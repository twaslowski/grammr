import { useState, useCallback } from 'react';
import { v4 as uuidv4 } from 'uuid';
import { ChatMessage, Message } from '@/chat/types/message';
import {fullLanguageName} from "@/lib/utils";
import {useLanguage} from "@/context/LanguageContext";

export function useChat() {
  const { languageLearned } = useLanguage();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [streamingMessage, setStreamingMessage] = useState<ChatMessage | null>(null);

  // todo revisit:
  // Rewrites break real-time response streaming, therefore we manually construct the URL here.
  // Unfortunately, calling the backend directly runs into CORS issues, so I'll write the proxy myself.
  // However, for now I'll just treat streaming as a secondary issue and get the feature done.
  const BACKEND_HOST = process.env.BACKEND_HOST || 'http://localhost:8080';

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
        console.log("learned language:", languageLearned);
        const response = await fetch(`/api/v1/chat`, {
          method: 'POST',
          body: JSON.stringify(promptMessages),
          headers: { 'Content-Type': 'application/json' },
        });

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

        setStreamingMessage({
          ...assistantTemplate,
          content: accumulated,
          status: 'failed',
        });
      }
    },
    [messages, languageLearned],
  );

  return {
    messages: streamingMessage ? [...messages, streamingMessage] : messages,
    sendMessage,
  };
}
