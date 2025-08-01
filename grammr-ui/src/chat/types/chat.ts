import { Message } from '@/chat/types/message';

export interface Chat {
  chatId: string;
  createdAt: Date;
  updatedAt: Date;
  summary: string;
}

export interface ChatInitializedDto {
  chat: Chat;
  response: Message;
}
