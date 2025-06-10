'use client';

import React, {
  createContext,
  ReactNode,
  useCallback,
  useContext,
  useEffect,
  useState,
} from 'react';
import { Chat } from '@/chat/types/chat';
import { useApi } from '@/hooks/useApi';

type ChatContextType = {
  chatId: string;
  setChatId: (id: string) => void;
  refreshChats: () => void;
  chats: Chat[];
};

const ChatContext = createContext<ChatContextType | undefined>(undefined);

export const ChatProvider = ({ children }: { children: ReactNode }) => {
  const [chatId, setChatId] = useState('');
  const [chats, setChats] = useState<Chat[]>([]);
  const { request } = useApi();

  const fetchChats = useCallback(async () => {
    try {
      const result = await request<Chat[]>('/api/v2/chat', {
        method: 'GET',
        credentials: 'include',
      });
      if (result) setChats(result);
    } catch (err) {
      console.error('Failed to fetch chats:', err);
    }
  }, [request]);

  useEffect(() => {
    void fetchChats();
  }, [fetchChats]);

  const refreshChats = useCallback(() => {
    void fetchChats();
  }, [fetchChats]);

  return (
    <ChatContext.Provider value={{ chatId, setChatId, chats: chats, refreshChats }}>
      {children}
    </ChatContext.Provider>
  );
};

export const useChat = (): ChatContextType => {
  const context = useContext(ChatContext);
  if (!context) {
    throw new Error('useChat must be used within a ChatProvider');
  }
  return context;
};
