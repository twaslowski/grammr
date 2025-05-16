import { SemanticTranslation } from '@/types/language';
import TokenType from '@/types/tokenType';

export default interface Analysis {
  sourcePhrase: string;
  semanticTranslation: SemanticTranslation;
  sourceLanguage: string;
  targetLanguage: string;
  analyzedTokens: TokenType[];
}
