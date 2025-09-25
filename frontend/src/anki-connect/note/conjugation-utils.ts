import { Paradigm } from '@/flashcard/types/paradigm';
import { Number as Num, Person } from '@/types/feature';
import { getFormFromInflections } from '@/anki-connect/note/util';

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
