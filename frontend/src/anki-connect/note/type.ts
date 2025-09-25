import { Flashcard } from '@/flashcard/types/flashcard';
import {
  BasicModelName,
  InflectionModelName,
  ConjugationModelName,
  DeclensionModelName,
} from '../model/model-names';
import { NoteFactory } from './note-factory';

export interface Note {
  id: string;
  modelName: string;
  deckName: string;
  fields: AnyField;
  tags?: string[];
}

interface BasicFields {
  id: string;
  front: string;
  back: string;
  notes: string;
}

interface InflectionFields {
  id: string;
  front: string;
  back: string;
  lemma: string;
  translation: string;
  table: string;
  notes: string;
}

interface DeclensionFields {
  id: string;
  front: string;
  back: string;
  lemma: string;
  translation: string;
  nominativeSingular: string;
  genitiveSingular: string;
  dativeSingular: string;
  accusativeSingular: string;
  nominativePlural: string;
  genitivePlural: string;
  dativePlural: string;
  accusativePlural: string;
  notes: string;
}

interface ConjugationFields {
  id: string;
  front: string;
  back: string;
  lemma: string;
  translation: string;
  firstPersonSingular: string;
  secondPersonSingular: string;
  thirdPersonSingular: string;
  firstPersonPlural: string;
  secondPersonPlural: string;
  thirdPersonPlural: string;
  mood: string;
  tense: string;
  voice: string;
  notes: string;
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
  fields: ConjugationFields;
  modelName: ConjugationModelName;
}

export interface DeclensionNote extends Note {
  fields: DeclensionFields;
  modelName: DeclensionModelName;
}

export type AnyField = BasicFields | DeclensionFields | InflectionFields | ConjugationFields;
export type AnyNote = BasicNote | InflectionNote | ConjugationNote | DeclensionNote;

export const fromFlashcard = (flashcard: Flashcard, deckName: string): AnyNote => {
  return NoteFactory.createFromFlashcard(flashcard, deckName);
};
