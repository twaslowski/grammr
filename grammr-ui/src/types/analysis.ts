import { SemanticTranslation } from '@/types/language';
import TokenType from '@/token/types/tokenType';

export interface Analysis {
  sourcePhrase: string;
  semanticTranslation: SemanticTranslation;
  sourceLanguage: string;
  targetLanguage: string;
  analyzedTokens: TokenType[];
}

export interface AnalysisV2 {
  analysisId: string;
  source: string;
  translation: string;
  sourceLanguage: string;
  targetLanguage: string;
  analysedTokens: TokenType[];
}
