import { Paradigm } from '@/flashcard/types/paradigm';

export interface Flashcard {
  question: string;
  answer: string;
  id: string;
  type: 'BASIC' | 'INFLECTION';
  status: 'CREATED' | 'UPDATED' | 'MARKED_FOR_DELETION' | 'SYNCED';
  paradigm?: Paradigm;
  pos?: string;
  createdTimestamp: string;
  updatedTimestamp: string;
}
