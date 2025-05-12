import { Feature } from '@/types/feature';
import TokenType from '@/types/tokenType';

export interface Inflection {
  lemma: string;
  inflected: string;
  features: Feature[];
}

export interface Inflections {
  lemma: string;
  partOfSpeech: string;
  inflections: Inflection[];
}

export interface InflectionsRequest {
  token: TokenType;
  languageCode: string;
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
