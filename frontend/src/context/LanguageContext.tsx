'use client';

import { createContext, ReactNode, useContext, useEffect, useState } from 'react';
import { ENGLISH, Language, RUSSIAN, languages } from '@/constant/languages';

type LanguageContextProps = {
  languageSpoken: Language;
  languageLearned: Language;
  setLanguageSpoken: (lang: Language) => void;
  setLanguageLearned: (lang: Language) => void;
  isReady: boolean;
};

const LanguageContext = createContext<LanguageContextProps | undefined>(undefined);

export function LanguageProvider({ children }: { children: ReactNode }) {
  const [languageSpoken, setLanguageSpokenState] = useState<Language>(ENGLISH);
  const [languageLearned, setLanguageLearnedState] = useState<Language>(RUSSIAN);
  const [isReady, setIsReady] = useState(false);

  // Helper to find Language by code
  const getLanguageByCode = (code: string): Language => {
    return languages.find((lang) => lang.code === code) || ENGLISH;
  };

  useEffect(() => {
    const savedSpoken = localStorage.getItem('languageSpoken');
    const savedLearned = localStorage.getItem('languageLearned');

    if (savedSpoken) setLanguageSpokenState(getLanguageByCode(savedSpoken));
    if (savedLearned) setLanguageLearnedState(getLanguageByCode(savedLearned));

    setIsReady(true);
  }, []);

  const setLanguageSpoken = (lang: Language) => {
    localStorage.setItem('languageSpoken', lang.code);
    setLanguageSpokenState(lang);
  };

  const setLanguageLearned = (lang: Language) => {
    localStorage.setItem('languageLearned', lang.code);
    setLanguageLearnedState(lang);
  };

  return (
    <LanguageContext.Provider
      value={{
        languageSpoken,
        languageLearned,
        setLanguageSpoken,
        setLanguageLearned,
        isReady,
      }}
    >
      {children}
    </LanguageContext.Provider>
  );
}

export function useLanguage() {
  const context = useContext(LanguageContext);
  if (!context) throw new Error('useLanguage must be used within a LanguageProvider');
  return context;
}
