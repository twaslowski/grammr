// Model names for Anki cards
export const MODEL_NAMES = {
  BASIC: 'Basic',
  INFLECTION: 'Inflection',
} as const;

// Type definitions for model names
export type BasicModelName = typeof MODEL_NAMES.BASIC;
export type InflectionModelName = typeof MODEL_NAMES.INFLECTION;
