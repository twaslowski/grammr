import {Message} from "@/chat/types/message";

export interface Chat {
  chatId: string;
  createdAt: number;
  updatedAt: number;
  summary: string;
}

export interface ChatInitializedDto {
  chat: Chat
  response: Message
}
