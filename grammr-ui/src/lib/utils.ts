import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

import TokenType from '@/types/tokenType';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export const capitalize = (s: string | undefined) => {
  if (typeof s !== 'string') return '';
  return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
};

export const tokenHasNoFeatures = (token: TokenType) => {
  return (
    !token.morphology.features ||
    token.morphology.features.filter((f) => f.type !== 'MISC').length === 0
  );
};

/**
 * Converts a language code to its full name
 * @param code ISO language code (e.g., 'en', 'de')
 * @returns Full language name
 */
export const getLanguageName = (code: string): string => {
  const languageMap: { [key: string]: string } = {
    en: 'English',
    de: 'German',
    ru: 'Russian',
    fr: 'French',
    es: 'Spanish',
    it: 'Italian',
    pt: 'Portuguese',
  };
  return languageMap[code] || code;
};
