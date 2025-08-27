export interface Language {
  code: string;
  name: string;
  flag: string;
  learnable: boolean;
}

export const RUSSIAN = {
  code: 'ru',
  name: 'Russian',
  flag: '🇷🇺',
  learnable: true,
};

export const ENGLISH = {
  code: 'en',
  name: 'English',
  flag: '🇬🇧',
  learnable: false,
};

export const GERMAN = {
  code: 'de',
  name: 'German',
  flag: '🇩🇪',
  learnable: false,
};

// Language data with ISO codes and display names
export const languages: Language[] = [RUSSIAN, ENGLISH, GERMAN];
