import {useEffect, useState} from 'react';
import {Message} from "@/chat/types/message";
import {useApi} from "@/hooks/useApi";
import {useChat} from "@/context/ChatContext";
import {ChatInitializedDto} from "@/chat/types/chat";
import {useLanguage} from "@/context/LanguageContext";

interface UseMessagesResult {
  messages: Message[];
  sendMessage: (message: Message) => Promise<void>;
  refreshMessages: () => void;
  isLoading: boolean;
}

const useMessages = (): UseMessagesResult => {
  const [messages, setMessages] = useState<Message[]>([]);
  const { languageLearned } = useLanguage();
  const {chatId, setChatId} = useChat();
  const {request, error, isLoading} = useApi();

  const refreshMessages = async () => {
    try {
      const response = await request<Message[]>(`/api/v2/chat/${chatId}/messages`);
      setMessages(response)
    } catch (error) {
      console.error(error);
    }
  };

  const sendMessage = async (message: Message) => {
    if (!chatId) {
      const chatInitializationDto = {
        message: message,
        languageCode: languageLearned
      }
      const chatInitialization = await request<ChatInitializedDto>('/api/v2/chat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(chatInitializationDto)
      });
      setChatId(chatInitialization.chat.chatId);
      void refreshMessages();
    } else {
      await request<Message>(`/api/v2/chat/${chatId}/messages`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({message})
      });
      await refreshMessages();
    }
  };

  useEffect(() => {
    if (chatId) {
      void refreshMessages();
    } else {
      setMessages([]);
    }
  }, [chatId]);

  return {messages, sendMessage, refreshMessages, isLoading};
};

export default useMessages;