import { Feature } from '@/types/feature';
import TokenType from '@/token/types/tokenType';

export interface Inflection {
  lemma: string;
  inflected: string;
  features: Feature[];
}

export interface Inflections {
  paradigmId: string;
  lemma: string;
  partOfSpeech: string;
  inflections: Inflection[];
}

export interface InflectionTableData {
  [key: string]: {
    singular: string;
    plural: string;
  };
}

export class InflectionsNotAvailableError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'InflectionsNotAvailableError';
  }
}
