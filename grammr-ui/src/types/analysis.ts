import TokenType from '@/token/types/tokenType';

export interface AnalysisV2 {
  analysisId: string;
  phrase: string;
  sourceLanguage: string;
  analysedTokens: TokenType[];
}
