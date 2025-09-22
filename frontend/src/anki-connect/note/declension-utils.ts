import { Paradigm } from '@/flashcard/types/paradigm';
import { Inflection } from '@/inflection/types/inflections';
import { Case, Feature, Number as Num, Person } from '@/types/feature';

export type FeatureIdentifier = Feature | Case | Num;

export function extractVerbForms(data: Paradigm) {
  if (data.partOfSpeech !== 'VERB') {
    throw new Error('extractVerbForms: Provided paradigm is not a verb');
  }
  const empty = '';

  // Extract canonical person/number combinations using the convenience wrapper
  const nominativeSingular =
    getFormFromInflections(data.inflections, [Case.NOM, Num.SINGULAR]) || empty;
  const genitiveSingular =
    getFormFromInflections(data.inflections, [Case.GEN, Num.SINGULAR]) || empty;
  const dativeSingular =
    getFormFromInflections(data.inflections, [Case.DAT, Num.SINGULAR]) || empty;
  const accusativeSingular =
    getFormFromInflections(data.inflections, [Case.ACC, Num.SINGULAR]) || empty;

  const nominativePlural =
    getFormFromInflections(data.inflections, [Case.NOM, Num.PLURAL]) || empty;
  const genitivePlural = getFormFromInflections(data.inflections, [Case.GEN, Num.PLURAL]) || empty;
  const dativePlural = getFormFromInflections(data.inflections, [Case.DAT, Num.PLURAL]) || empty;
  const accusativePlural =
    getFormFromInflections(data.inflections, [Case.ACC, Num.PLURAL]) || empty;

  return {
    nominativeSingular,
    genitiveSingular,
    dativeSingular,
    accusativeSingular,
    nominativePlural,
    genitivePlural,
    dativePlural,
    accusativePlural,
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
