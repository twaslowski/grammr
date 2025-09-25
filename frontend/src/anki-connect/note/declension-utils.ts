import { Paradigm } from '@/flashcard/types/paradigm';
import { Case, Number as Num } from '@/types/feature';
import { getFormFromInflections } from '@/anki-connect/note/util';

export function extractCases(data: Paradigm) {
  const empty = '';

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
