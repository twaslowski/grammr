import { Inflection } from '@/inflection/types/inflections';

export interface Paradigm {
  id: string;
  languageCode: string;
  partOfSpeech: string;
  inflections: Inflection[];
}
