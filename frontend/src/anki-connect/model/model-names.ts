export const MODEL_NAMES = {
  BASIC: 'GrammrBasic',
  INFLECTION_GENERIC: 'GenericInflection',
  CONJUGATION: 'ConjugationTable',
  DECLENSION: 'DeclensionTable',
} as const;

export type BasicModelName = typeof MODEL_NAMES.BASIC;
export type InflectionModelName = typeof MODEL_NAMES.INFLECTION_GENERIC;
export type ConjugationModelName = typeof MODEL_NAMES.CONJUGATION;
export type DeclensionModelName = typeof MODEL_NAMES.DECLENSION;
