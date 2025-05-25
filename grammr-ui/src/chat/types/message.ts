export type Message = {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: number;
};

export type ChatMessage = Message & {
  status?: 'streaming' | 'sent' | 'failed';
};
