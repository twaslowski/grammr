import { Flashcard } from '@/flashcard/types/flashcard';
import {
  MODEL_NAMES,
  BasicModelName,
  InflectionModelName,
  ConjugationModelName,
  DeclensionModelName,
} from '@/constant/constants';

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
  translation: string;
  partOfSpeech: string;
  inflections: string;
}

export interface BasicNote extends Note {
  fields: BasicFields;
  modelName: BasicModelName;
}

export interface InflectionNote extends Note {
  fields: InflectionFields;
  modelName: InflectionModelName;
}

export interface ConjugationNote extends Note {
  fields: InflectionFields;
  modelName: ConjugationModelName;
}

export interface DeclensionNote extends Note {
  fields: InflectionFields;
  modelName: DeclensionModelName;
}

export type AnyField = BasicFields | InflectionFields;
export type AnyNote = BasicNote | InflectionNote | ConjugationNote | DeclensionNote;

export const basicNote = (flashcard: Flashcard, deckName: string): BasicNote => {
  return {
    id: flashcard.id,
    fields: {
      front: flashcard.question,
      back: flashcard.answer,
    },
    modelName: MODEL_NAMES.BASIC,
    deckName: deckName,
  };
};

export const conjugationNote = (flashcard: Flashcard, deckName: string): ConjugationNote => {
  if (!flashcard.paradigm) {
    throw new Error('Conjugation note requires paradigm data');
  }

  return {
    id: flashcard.id,
    fields: {
      front: flashcard.question,
      back: flashcard.answer,
      lemma: flashcard.paradigm.lemma,
      translation: flashcard.answer,
      partOfSpeech: flashcard.paradigm.partOfSpeech,
      inflections: JSON.stringify(flashcard.paradigm.inflections),
    },
    modelName: MODEL_NAMES.CONJUGATION,
    deckName: deckName,
  };
};

export const declensionNote = (flashcard: Flashcard, deckName: string): DeclensionNote => {
  if (!flashcard.paradigm) {
    throw new Error('Declension note requires paradigm data');
  }

  return {
    id: flashcard.id,
    fields: {
      front: flashcard.question,
      back: flashcard.answer,
      lemma: flashcard.paradigm.lemma,
      translation: flashcard.answer,
      partOfSpeech: flashcard.paradigm.partOfSpeech,
      inflections: JSON.stringify(flashcard.paradigm.inflections),
    },
    modelName: MODEL_NAMES.DECLENSION,
    deckName: deckName,
  };
};

const getInflectionNoteType = (partOfSpeech: string): 'conjugation' | 'declension' | 'general' => {
  const pos = partOfSpeech.toLowerCase();

  if (pos === 'verb' || pos === 'aux') {
    return 'conjugation';
  } else if (pos === 'noun' || pos === 'adj') {
    return 'declension';
  } else {
    return 'general';
  }
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
      translation: flashcard.answer,
      partOfSpeech: flashcard.paradigm.partOfSpeech,
      inflections: JSON.stringify(flashcard.paradigm.inflections),
    },
    modelName: MODEL_NAMES.INFLECTION,
    deckName: deckName,
  };
};

export const fromFlashcard = (flashcard: Flashcard, deckName: string): AnyNote => {
  if (flashcard.type === 'INFLECTION') {
    if (!flashcard.paradigm) {
      throw new Error('Inflection flashcard requires paradigm data');
    }

    const noteType = getInflectionNoteType(flashcard.paradigm.partOfSpeech);

    switch (noteType) {
      case 'conjugation':
        return conjugationNote(flashcard, deckName);
      case 'declension':
        return declensionNote(flashcard, deckName);
      default:
        return inflectionNote(flashcard, deckName);
    }
  } else {
    return basicNote(flashcard, deckName);
  }
};
