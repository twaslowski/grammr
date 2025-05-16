export default interface AnalysisRequest {
  phrase: string;
  userLanguageSpoken: string;
  userLanguageLearned: string;
  performSemanticTranslation: boolean;
}
