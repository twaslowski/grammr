import { Flashcard } from '@/flashcard/types/flashcard';

export default interface Deck {
  id: number;
  name: string;
  description: string;
  flashcards: Flashcard[];
  createdTimestamp: string;
  updatedTimestamp: string;
}
