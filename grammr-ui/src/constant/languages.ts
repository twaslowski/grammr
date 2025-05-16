// Language data with ISO codes and display names
export const languages = [
  { code: 'en', name: 'English', flag: '🇬🇧' },
  { code: 'de', name: 'German', flag: '🇩🇪' },
  { code: 'ru', name: 'Russian', flag: '🇷🇺' },
  { code: 'it', name: 'Italian', flag: '🇮🇹' },
  { code: 'es', name: 'Spanish', flag: '🇪🇸' },
  { code: 'pt', name: 'Portuguese', flag: '🇵🇹' },
  { code: 'fr', name: 'French', flag: '🇫🇷' },
];

export const languageFeatures = {
  German: {
    sentenceTranslation: true,
    literalWordTranslation: true,
    morphologicalAnalysis: true,
    verbConjugation: false,
    nounDeclension: false,
  },
  English: {
    sentenceTranslation: true,
    literalWordTranslation: true,
    morphologicalAnalysis: true,
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
  Spanish: {
    sentenceTranslation: true,
    literalWordTranslation: true,
    morphologicalAnalysis: true,
    verbConjugation: true,
    nounDeclension: false,
  },
  French: {
    sentenceTranslation: true,
    literalWordTranslation: true,
    morphologicalAnalysis: true,
    verbConjugation: true,
    nounDeclension: false,
  },
  Portuguese: {
    sentenceTranslation: true,
    literalWordTranslation: true,
    morphologicalAnalysis: true,
    verbConjugation: true,
    nounDeclension: false,
  },
  Italian: {
    sentenceTranslation: true,
    literalWordTranslation: true,
    morphologicalAnalysis: true,
    verbConjugation: true,
    nounDeclension: false,
  },
};
