import { useEffect, useState, useCallback } from 'react';
import { Message } from '@/chat/types/message';
import { ApiError, useApi } from '@/hooks/useApi';
import { ChatInitializedDto } from '@/chat/types/chat';
import { useLanguage } from '@/context/LanguageContext';

interface UseMessagesResult {
  messages: Message[];
  sendMessage: (message: string) => Promise<void>;
  startChat: (message: string) => Promise<string>;
  refreshMessages: () => void;
  isLoading: boolean;
  error: ApiError | null;
}

const useMessages = (chatId: string): UseMessagesResult => {
  const [messages, setMessages] = useState<Message[]>([]);
  const { languageLearned } = useLanguage();
  const { request, error, isLoading } = useApi();

  const refreshMessages = useCallback(async () => {
    try {
      const response = await request<Message[]>(`/api/v2/chat/${chatId}/messages`);
      setMessages(response);
    } catch (error) {
      console.error(error);
    }
  }, [chatId, request]);

  const startChat = async (message: string): Promise<string> => {
    const tempUserMessage = createTempMessage(message);
    setMessages((prevMessages) => [...prevMessages, tempUserMessage]);

    const chatInitializationDto = {
      message: message,
      languageCode: languageLearned,
    };
    const chatInitialization = await request<ChatInitializedDto>('/api/v2/chat', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(chatInitializationDto),
    });
    return chatInitialization.chat.chatId;
  };

  const sendMessage = async (message: string): Promise<void> => {
    const tempUserMessage = createTempMessage(message);
    setMessages((prevMessages) => [...prevMessages, tempUserMessage]);

    await request<Message>(`/api/v2/chat/${chatId}/messages`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: message,
    });
    await refreshMessages();
  };

  const createTempMessage = (message: string): Message => {
    // This temporary message displays immediately in the UI, gets overwritten with refreshMessages()
    return {
      id: crypto.randomUUID(),
      role: 'USER',
      content: message,
      date: new Date().toISOString(),
    };
  };

  useEffect(() => {
    if (chatId) {
      void refreshMessages();
    } else {
      setMessages([]);
    }
  }, [chatId, refreshMessages]);

  return { messages, sendMessage, startChat, refreshMessages, isLoading, error };
};

export default useMessages;
