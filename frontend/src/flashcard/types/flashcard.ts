import { Paradigm } from '@/flashcard/types/paradigm';

export interface Flashcard {
  question: string;
  answer: string;
  id: string;
  status: 'CREATED' | 'UPDATED' | 'MARKED_FOR_DELETION' | 'SYNCED';
  paradigm: Paradigm | null;
  createdTimestamp: string;
  updatedTimestamp: string;
}
