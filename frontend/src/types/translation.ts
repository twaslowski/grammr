import { AnalysisV2 } from '@/types/analysis';

export interface Translation {
  source: string;
  translation: string;
  sourceLanguage: string;
  targetLanguage: string;
  analysis: AnalysisV2;
}
