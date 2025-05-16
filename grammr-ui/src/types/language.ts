import { Feature } from '@/types/feature';

export interface SemanticTranslation {
  sourcePhrase: string;
  translatedPhrase: string;
}

export interface TokenTranslation {
  source: string;
  translation: string;
}

export interface TokenMorphology {
  text: string;
  lemma: string;
  pos: string;
  features: Feature[];

  stringify_features(): string;
}
