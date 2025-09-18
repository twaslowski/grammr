import { Flashcard } from '@/flashcard/types/flashcard';
import {
  BasicModelName,
  InflectionModelName,
  ConjugationModelName,
  DeclensionModelName,
} from '../model/model-names';
import { NoteFactory } from '@/anki-connect/note-factory';

export interface Note {
  id: string;
  modelName: string;
  deckName: string;
  fields: AnyField;
  tags?: string[];
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

export const fromFlashcard = (flashcard: Flashcard, deckName: string): AnyNote => {
  return NoteFactory.createFromFlashcard(flashcard, deckName);
};
