import { useState, useCallback, useEffect } from 'react';
import { v4 as uuidv4 } from 'uuid';
import { ChatMessage, Message } from '@/chat/types/message';
import { fullLanguageName } from '@/lib/utils';
import { useLanguage } from '@/context/LanguageContext';

export function useChat() {
  const { languageLearned } = useLanguage();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [streamingMessage, setStreamingMessage] = useState<ChatMessage | null>(null);

  const BACKEND_HOST = process.env.BACKEND_HOST || 'http://localhost:8080';

  // Retrieve messages after mounting (instead of defaulting the useState) to avoid hydration errors
  useEffect(() => {
    try {
      const stored = sessionStorage.getItem('chatMessages');
      if (stored) {
        setMessages(JSON.parse(stored));
      }
    } catch (error) {
      console.error('Failed to parse chat messages from session storage:', error);
      sessionStorage.removeItem('chatMessages');
    }
  }, []);

  // Persist messages to session storage whenever they change.
  useEffect(() => {
    sessionStorage.setItem('chatMessages', JSON.stringify(messages));
  }, [messages]);

  const sendMessage = useCallback(
    async (input: Message) => {
      const initializeMessages = (): ChatMessage => {
        const languageName = fullLanguageName(languageLearned);
        return {
          id: uuidv4(),
          role: 'system',
          content: `You are a friendly and engaging language tutor. You converse naturally with the user in ${languageName}, adapting to their level. Keep responses appropriate for learners and encourage them to reply in the same language.`,
          timestamp: Date.now(),
          status: 'sent',
        };
      };

      const userMsg: ChatMessage = {
        ...input,
        id: uuidv4(),
        status: 'sent',
      };

      setMessages((prev) => [...prev, userMsg]);

      const assistantId = uuidv4();
      let accumulated = '';

      const assistantTemplate: ChatMessage = {
        id: assistantId,
        role: 'assistant',
        content: '',
        timestamp: Date.now(),
        status: 'streaming',
      };

      setStreamingMessage(assistantTemplate);

      let promptMessages = [...messages, userMsg]; // might be stale
      if (messages.length === 0) {
        promptMessages = [initializeMessages(), ...promptMessages];
      }

      try {
        const response = await fetch(`/api/v1/chat`, {
          method: 'POST',
          body: JSON.stringify(promptMessages),
          headers: { 'Content-Type': 'application/json' },
        });

        if (response.status !== 200)
          throw new Error(response.statusText || 'Failed to fetch response');
        if (!response.body) throw new Error('No stream body');

        const reader = response.body.pipeThrough(new TextDecoderStream()).getReader();
        let accumulated = '';

        while (true) {
          const { value, done } = await reader.read();
          if (done) break;

          accumulated += value;
          setStreamingMessage({
            ...assistantTemplate,
            content: accumulated,
          });
        }

        // Finalize message
        const finalAssistantMessage: ChatMessage = {
          ...assistantTemplate,
          content: accumulated,
          status: 'sent',
        };

        setMessages((prev) => [...prev, finalAssistantMessage]);
        setStreamingMessage(null);
      } catch (err) {
        console.error(err);

        const finalAssistantMessage: ChatMessage = {
          ...assistantTemplate,
          content: 'An error occurred while processing your request. Please try again later.',
          status: 'failed',
        };

        setMessages((prev) => [...prev, finalAssistantMessage]);
        setStreamingMessage(null);
      }
    },
    [messages, languageLearned],
  );

  return {
    messages: streamingMessage ? [...messages, streamingMessage] : messages,
    sendMessage,
  };
}
