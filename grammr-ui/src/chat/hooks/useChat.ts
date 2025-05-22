import { useState } from 'react';
import { Message } from '@/chat/types/message';
import { ApiError } from '@/hooks/useApi';

export const useChat = () => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [error, setError] = useState<ApiError | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const generateId = () => Math.random().toString(36).substring(2, 10);

  const sendMessage = async (message: string) => {
    const userMessage = {
      id: generateId(),
      role: 'user' as const,
      content: message,
    };

    const updatedMessages = [...messages, userMessage];
    setMessages((prev) => [...prev, userMessage]);
    setIsLoading(true);

    try {
      const result = await fetch('/api/v1/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedMessages),
      });

      if (!result.ok) {
        throw new Error(result.statusText);
      }

      const messageResult = await result.text();
      const placeholderId = generateId();
      const placeholderMessage = {
        id: placeholderId,
        role: 'assistant' as const,
        content: messageResult,
      };

      setMessages((prev) => [...prev, placeholderMessage]);
    } catch (err: any) {
      setError({ code: 500, message: err.message || 'Unknown error' });
    } finally {
      setIsLoading(false);
    }
  };

  return { messages, sendMessage, isLoading, error };
};
