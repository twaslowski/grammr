import { Flashcard } from '@/flashcard/types/flashcard';

export interface Note {
  id: string;
  fields: Fields;
  modelName: string;
  deckName: string;
}

interface Fields {
  front: string;
  back: string;
}

export const fromFlashcard = (flashcard: Flashcard, deckName: string): Note => {
  return {
    id: flashcard.id,
    fields: {
      front: flashcard.question,
      back: flashcard.answer,
    },
    modelName: 'Basic',
    deckName: deckName,
  };
};
