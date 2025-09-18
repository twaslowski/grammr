// Model names for Anki cards
export const MODEL_NAMES = {
  BASIC: 'GrammrBasic',
  INFLECTION: 'GenericInflection',
  CONJUGATION: 'ConjugationTable',
  DECLENSION: 'DeclensionTable',
} as const;

// Type definitions for model names
export type BasicModelName = typeof MODEL_NAMES.BASIC;
export type InflectionModelName = typeof MODEL_NAMES.INFLECTION;
export type ConjugationModelName = typeof MODEL_NAMES.CONJUGATION;
export type DeclensionModelName = typeof MODEL_NAMES.DECLENSION;
