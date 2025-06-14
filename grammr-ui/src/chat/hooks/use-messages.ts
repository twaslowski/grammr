import {useCallback, useEffect, useState} from 'react';
import {Message} from '@/chat/types/message';
import {ApiError, useApi} from '@/hooks/useApi';
import {v4} from 'uuid';
import {ChatInitializedDto} from '@/chat/types/chat';
import {useLanguage} from '@/context/LanguageContext';

interface UseMessagesResult {
  messages: Message[];
  sendMessage: (message: string) => Promise<void>;
  startChat: (message: string) => Promise<string>;
  refreshMessages: () => void;
  isLoading: boolean;
  error: ApiError | null;
}

/**
 * Custom hook to manage chat messages for a specific chat ID.
 *
 * @param {string} chatId - The ID of the chat to manage messages for.
 * @returns {UseMessagesResult} An object containing:
 * - `messages`: The list of messages in the chat.
 * - `sendMessage`: A function to send a new message.
 * - `startChat`: A function to initialize a chat with a message.
 * - `refreshMessages`: A function to refresh the list of messages.
 * - `isLoading`: A boolean indicating if a request is in progress.
 * - `error`: An error object if a request fails.
 *
 * @example
 * const { messages, sendMessage, startChat, refreshMessages, isLoading, error } = useMessages(chatId);
 * sendMessage("Hello!");
 */
const useMessages = (chatId: string): UseMessagesResult => {
  const [messages, setMessages] = useState<Message[]>([]);
  const {languageLearned} = useLanguage();
  const {request, error, isLoading} = useApi();

  const refreshMessages = useCallback(async () => {
    try {
      const response = await request<Message[]>(`/api/v2/chat/${chatId}/messages`);
      setMessages(response);
    } catch (error) {
      console.error(error);
    }
  }, [chatId, request]);

  const startChat = async (message: string): Promise<string> => {
    createTempMessage(message);

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
    createTempMessage(message);

    await request<Message>(`/api/v2/chat/${chatId}/messages`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: message,
    });
    await refreshMessages();
  };

  const createTempMessage = (message: string): void => {
    // This temporary message displays immediately in the UI, gets overwritten with refreshMessages()
    const tempUserMessage: Message = {
      id: v4(),
      role: 'USER',
      content: message,
      date: new Date().toISOString(),
    };
    setMessages((prevMessages) => [...prevMessages, tempUserMessage]);
  };

  useEffect(() => {
    if (chatId) {
      void refreshMessages();
    } else {
      setMessages([]);
    }
  }, [chatId, refreshMessages]);

  return {messages, sendMessage, startChat, refreshMessages, isLoading, error};
};

export default useMessages;
