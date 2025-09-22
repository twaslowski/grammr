import { Paradigm } from '@/flashcard/types/paradigm';
import { Inflection } from '@/inflection/types/inflections';
import { Feature, Number as Num, Person } from '@/types/feature';

export type FeatureIdentifier = Feature | Person | Num;

export function extractVerbForms(data: Paradigm) {
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
