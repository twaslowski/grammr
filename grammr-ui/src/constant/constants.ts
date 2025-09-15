// Model names for Anki cards
export const MODEL_NAMES = {
  BASIC: 'Basic',
  INFLECTION: 'GenericInflection',
  CONJUGATION: 'ConjugationTable',
  DECLENSION: 'DeclensionTable',
} as const;

// Model versioning system
export const MODEL_VERSIONS = {
  CONJUGATION: 'v1.0.0',
  DECLENSION: 'v1.0.0',
  INFLECTION: 'v1.0.0',
} as const;

// Versioned model names (used for actual Anki model creation)
export const VERSIONED_MODEL_NAMES = {
  BASIC: MODEL_NAMES.BASIC,
  INFLECTION: `${MODEL_NAMES.INFLECTION}_${MODEL_VERSIONS.INFLECTION}`,
  CONJUGATION: `${MODEL_NAMES.CONJUGATION}_${MODEL_VERSIONS.CONJUGATION}`,
  DECLENSION: `${MODEL_NAMES.DECLENSION}_${MODEL_VERSIONS.DECLENSION}`,
} as const;

// Migration tracking
export const MODEL_MIGRATION_STATUS = {
  NEEDS_MIGRATION: 'needs_migration',
  UP_TO_DATE: 'up_to_date',
  NOT_FOUND: 'not_found',
} as const;

// Type definitions for model names
export type BasicModelName = typeof MODEL_NAMES.BASIC;
export type InflectionModelName = typeof MODEL_NAMES.INFLECTION;
export type ConjugationModelName = typeof MODEL_NAMES.CONJUGATION;
export type DeclensionModelName = typeof MODEL_NAMES.DECLENSION;
