import { Flashcard } from '@/flashcard/types/flashcard';

export interface Note {
  id: string;
  modelName: string;
  deckName: string;
  fields: AnyField;
}

interface BasicFields {
  front: string;
  back: string;
}

interface InflectionFields {
  front: string;
  back: string;
  lemma: string;
  languageCode: string;
  partOfSpeech: string;
  inflections: string;
}

export interface BasicNote extends Note {
  fields: BasicFields;
  modelName: 'Basic';
}

export interface InflectionNote extends Note {
  fields: InflectionFields;
  modelName: 'Inflection';
}

export type AnyField = BasicFields | InflectionFields;
export type AnyNote = BasicNote | InflectionNote;

export const basicNote = (flashcard: Flashcard, deckName: string): BasicNote => {
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

export const inflectionNote = (flashcard: Flashcard, deckName: string): InflectionNote => {
  if (!flashcard.paradigm) {
    throw new Error('Inflection note requires paradigm data');
  }

  return {
    id: flashcard.id,
    fields: {
      front: flashcard.question,
      back: flashcard.answer,
      lemma: flashcard.paradigm.lemma,
      languageCode: flashcard.paradigm.languageCode,
      partOfSpeech: flashcard.paradigm.partOfSpeech,
      inflections: JSON.stringify(flashcard.paradigm.inflections),
    },
    modelName: 'Inflection',
    deckName: deckName,
  };
};

export const fromFlashcard = (flashcard: Flashcard, deckName: string): AnyNote => {
  if (flashcard.type === 'INFLECTION') {
    return inflectionNote(flashcard, deckName);
  } else {
    return basicNote(flashcard, deckName);
  }
};
