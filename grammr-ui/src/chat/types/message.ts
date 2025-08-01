export type Message = {
  id: string;
  role: 'USER' | 'ASSISTANT' | 'SYSTEM';
  chatId: string;
  content: string;
  date: string;
  analysisId?: string;
};
