import { ConjugationData } from './conjugation-templates';
import { VERSIONED_MODEL_NAMES } from '@/constant/constants';

// Utility functions for creating conjugation cards

/**
 * Creates a conjugation data structure for a verb
 * @param persons Array of person labels (e.g., ['1st', '2nd', '3rd'])
 * @param numbers Array of number labels (e.g., ['singular', 'plural'])
 * @param conjugations Object mapping number -> person -> conjugated form
 */
export function createConjugationData(
  persons: string[],
  numbers: string[],
  conjugations: Record<string, Record<string, string>>,
): ConjugationData {
  return {
    persons,
    numbers,
    forms: conjugations,
  };
}

/**
 * Helper for creating standard person/number conjugations
 * Common for Romance languages like Spanish, French, Italian
 */
export function createStandardConjugation(conjugations: {
  singular: {
    first: string;
    second: string;
    third: string;
  };
  plural: {
    first: string;
    second: string;
    third: string;
  };
}): ConjugationData {
  return createConjugationData(['1st', '2nd', '3rd'], ['singular', 'plural'], {
    singular: {
      '1st': conjugations.singular.first,
      '2nd': conjugations.singular.second,
      '3rd': conjugations.singular.third,
    },
    plural: {
      '1st': conjugations.plural.first,
      '2nd': conjugations.plural.second,
      '3rd': conjugations.plural.third,
    },
  });
}

/**
 * Helper for creating German-style conjugations
 * Includes formal/informal distinctions
 */
export function createGermanConjugation(conjugations: {
  ich: string;
  du: string;
  er_sie_es: string;
  wir: string;
  ihr: string;
  sie_Sie: string;
}): ConjugationData {
  return createConjugationData(['ich', 'du', 'er/sie/es', 'wir', 'ihr', 'sie/Sie'], [''], {
    '': {
      ich: conjugations.ich,
      du: conjugations.du,
      'er/sie/es': conjugations.er_sie_es,
      wir: conjugations.wir,
      ihr: conjugations.ihr,
      'sie/Sie': conjugations.sie_Sie,
    },
  });
}

/**
 * Helper for creating Russian-style conjugations
 * Using Cyrillic pronouns
 */
export function createRussianConjugation(conjugations: {
  ya: string; // я
  ty: string; // ты
  on_ona_ono: string; // он/она/оно
  my: string; // мы
  vy: string; // вы
  oni: string; // они
}): ConjugationData {
  return createConjugationData(['я', 'ты', 'он/она/оно', 'мы', 'вы', 'они'], [''], {
    '': {
      я: conjugations.ya,
      ты: conjugations.ty,
      'он/она/оно': conjugations.on_ona_ono,
      мы: conjugations.my,
      вы: conjugations.vy,
      они: conjugations.oni,
    },
  });
}

/**
 * Creates an Anki note for a conjugation card
 */
export async function createConjugationNote(
  deckName: string,
  lemma: string,
  translation: string,
  tense: string,
  mood: string,
  conjugationData: ConjugationData,
  language: string,
  notes?: string,
): Promise<void> {
  const noteData = {
    deckName,
    modelName: VERSIONED_MODEL_NAMES.CONJUGATION,
    fields: {
      lemma,
      translation,
      tense,
      mood,
      conjugations: JSON.stringify(conjugationData),
      language,
      notes: notes || '',
    },
    tags: [`conjugation`, `${language}`, `${tense}`, `${mood}`],
  };

  const response = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'addNote',
      version: 6,
      params: {
        note: noteData,
      },
    }),
  });

  if (!response.ok) {
    throw new Error('Failed to create conjugation note');
  }

  const data = await response.json();
  if (data.error) {
    throw new Error(data.error);
  }
}

/**
 * Creates a cloze conjugation note where forms are hidden
 */
export async function createConjugationClozeNote(
  deckName: string,
  lemma: string,
  translation: string,
  tense: string,
  mood: string,
  conjugationData: ConjugationData,
  language: string,
  notes?: string,
): Promise<void> {
  const noteData = {
    deckName,
    modelName: `${VERSIONED_MODEL_NAMES.CONJUGATION}_Cloze`,
    fields: {
      lemma,
      translation,
      tense,
      mood,
      conjugations: JSON.stringify(conjugationData),
      language,
      notes: notes || '',
    },
    tags: [`conjugation-cloze`, `${language}`, `${tense}`, `${mood}`],
  };

  const response = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'addNote',
      version: 6,
      params: {
        note: noteData,
      },
    }),
  });

  if (!response.ok) {
    throw new Error('Failed to create conjugation cloze note');
  }

  const data = await response.json();
  if (data.error) {
    throw new Error(data.error);
  }
}

// Example usage functions for common languages

/**
 * Example: Create a Spanish present tense conjugation card
 */
export async function createSpanishPresentConjugation(
  deckName: string,
  lemma: string,
  translation: string,
  conjugations: {
    singular: { first: string; second: string; third: string };
    plural: { first: string; second: string; third: string };
  },
): Promise<void> {
  const conjugationData = createStandardConjugation(conjugations);
  await createConjugationNote(
    deckName,
    lemma,
    translation,
    'Present',
    'Indicative',
    conjugationData,
    'Spanish',
  );
}

/**
 * Example: Create a German present tense conjugation card
 */
export async function createGermanPresentConjugation(
  deckName: string,
  lemma: string,
  translation: string,
  conjugations: {
    ich: string;
    du: string;
    er_sie_es: string;
    wir: string;
    ihr: string;
    sie_Sie: string;
  },
): Promise<void> {
  const conjugationData = createGermanConjugation(conjugations);
  await createConjugationNote(
    deckName,
    lemma,
    translation,
    'Präsens',
    'Indikativ',
    conjugationData,
    'German',
  );
}
