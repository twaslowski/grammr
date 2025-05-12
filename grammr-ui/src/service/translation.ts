import { TokenTranslation } from '@/types';

/**
 * Fetches the translation of a word in a given context, i.e. a phrase
 * @param context The sentence within which the word exists
 * @param word a single word
 * @param languageCode the language into which to translate said word
 */
export const fetchTranslation = async (
  context: string,
  word: string,
  languageCode: string,
): Promise<TokenTranslation> => {
  const response = await fetch('/api/v1/translate/word/context', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      phrase: context,
      word: word,
      targetLanguage: languageCode,
    }),
  });
  return (await response.json()) as TokenTranslation;
};
