// Language data with ISO codes and display names
export const languages = [
  { code: 'de', name: 'German', flag: '🇩🇪', learnable: false },
  { code: 'en', name: 'English', flag: '🇬🇧', learnable: false },
  { code: 'ru', name: 'Russian', flag: '🇷🇺', learnable: true },
];

export const languageFeatures = {
  German: {
    sentenceTranslation: true,
    literalWordTranslation: true,
    morphologicalAnalysis: false,
    verbConjugation: false,
    nounDeclension: false,
  },
  English: {
    sentenceTranslation: true,
    literalWordTranslation: true,
    morphologicalAnalysis: false,
    verbConjugation: false,
    nounDeclension: false,
  },
  Russian: {
    sentenceTranslation: true,
    literalWordTranslation: true,
    morphologicalAnalysis: true,
    verbConjugation: true,
    nounDeclension: true,
  },
};
