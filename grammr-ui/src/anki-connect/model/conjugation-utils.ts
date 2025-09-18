import { MODEL_NAMES } from './model-names';
import { Paradigm } from '@/flashcard/types/paradigm';
import { Inflection } from '@/inflection/types/inflections';
import { Feature, Number as Num, Person } from '@/types/feature';

export type FeatureIdentifier = Feature | Person | Num;

function extractVerbForms(data: Paradigm) {
  if (data.partOfSpeech !== 'VERB') {
    throw new Error('extractVerbForms: Provided paradigm is not a verb');
  }
  const empty = '';

  // Extract canonical person/number combinations using the convenience wrapper
  const firstPersonSingular =
    getFormFromInflections(data.inflections, [Person.FIRST, Num.SINGULAR]) || empty;
  const secondPersonSingular =
    getFormFromInflections(data.inflections, [Person.SECOND, Num.SINGULAR]) || empty;
  const thirdPersonSingular =
    getFormFromInflections(data.inflections, [Person.THIRD, Num.SINGULAR]) || empty;

  const firstPersonPlural =
    getFormFromInflections(data.inflections, [Person.FIRST, Num.PLURAL]) || empty;
  const secondPersonPlural =
    getFormFromInflections(data.inflections, [Person.SECOND, Num.PLURAL]) || empty;
  const thirdPersonPlural =
    getFormFromInflections(data.inflections, [Person.THIRD, Num.PLURAL]) || empty;

  return {
    firstPersonSingular,
    secondPersonSingular,
    thirdPersonSingular,
    firstPersonPlural,
    secondPersonPlural,
    thirdPersonPlural,
  };
}

/**
 * Creates an Anki note for a conjugation card
 */
export async function createConjugationNote(
  deckName: string,
  lemma: string,
  translation: string,
  paradigm: Paradigm,
  language: string,
): Promise<void> {
  const flattened = extractVerbForms(paradigm);

  const noteData = {
    deckName,
    modelName: MODEL_NAMES.CONJUGATION,
    fields: {
      front: lemma,
      back: translation,
      lemma,
      firstPersonSingular: flattened.firstPersonSingular,
      secondPersonSingular: flattened.secondPersonSingular,
      thirdPersonSingular: flattened.thirdPersonSingular,
      firstPersonPlural: flattened.firstPersonPlural,
      secondPersonPlural: flattened.secondPersonPlural,
      thirdPersonPlural: flattened.thirdPersonPlural,
      language,
    },
    tags: [`conjugation`, `${language}`],
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

export function getFormFromInflections(
  inflections: Inflection[],
  features: FeatureIdentifier[],
): string | undefined {
  const match = inflections.find((inf) =>
    features.every((desired) =>
      inf.features.some((f) => {
        if (typeof desired === 'object') {
          return f.value === desired.value || f.type === desired.type;
        }

        return f.value === desired || f.fullIdentifier === desired || f.type === desired;
      }),
    ),
  );

  return match?.inflected;
}
