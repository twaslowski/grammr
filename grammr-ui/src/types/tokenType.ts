import { TokenMorphology, TokenTranslation } from '@/types/language';

export default interface TokenType {
  index: number;
  text: string;
  morphology: TokenMorphology;
  translation: TokenTranslation;
}
