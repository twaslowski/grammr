import Analysis from '@/types/analysis';

export type Message = {
  id: string;
  role: 'user' | 'assistant' | 'system';
  content: string;
  analysis: Analysis | null;
  timestamp: number;
};

export type ChatMessage = Message & {
  status?: 'streaming' | 'sent' | 'failed';
};
