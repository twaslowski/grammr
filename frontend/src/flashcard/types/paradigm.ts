import { Inflection } from '@/inflection/types/inflections';

export interface Paradigm {
  paradigmId: string;
  lemma: string;
  languageCode: string;
  partOfSpeech: string;
  inflections: Inflection[];
}
