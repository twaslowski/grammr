import { useState, useCallback } from 'react';
import { v4 as uuidv4 } from 'uuid';
import { ChatMessage, Message } from '@/chat/types/message';

export function useChat() {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [streamingMessage, setStreamingMessage] = useState<ChatMessage | null>(null);

  // Rewrites break real-time response streaming, therefore we manually construct the URL here
  const BACKEND_HOST = process.env.BACKEND_HOST || 'http://localhost:8080';

  const sendMessage = useCallback(
    async (input: Message) => {
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

      try {
        const promptMessages = [...messages, userMsg]; // might be stale
        const response = await fetch(`${BACKEND_HOST}/api/v1/chat`, {
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
    [messages],
  );

  return {
    messages: streamingMessage ? [...messages, streamingMessage] : messages,
    sendMessage,
  };
}
