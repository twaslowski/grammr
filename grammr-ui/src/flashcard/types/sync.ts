import { Flashcard } from '@/flashcard/types/flashcard';

export interface Sync {
  syncId: string;
  flashcards: Flashcard[];
  createdTimestamp: string;
}
