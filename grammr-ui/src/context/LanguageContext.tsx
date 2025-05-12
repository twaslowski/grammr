'use client';

import React, { createContext, ReactNode, useContext, useState } from 'react';

interface LanguageContextProps {
  languageSpoken: string;
  languageLearned: string;
  setLanguageSpoken: (language: string) => void;
  setLanguageLearned: (language: string) => void;
}

const LanguageContext = createContext<LanguageContextProps | undefined>(
  undefined,
);

export const LanguageProvider = ({ children }: { children: ReactNode }) => {
  const [languageSpoken, setLanguageSpoken] = useState('en');
  const [languageLearned, setLanguageLearned] = useState('es');

  return (
    <LanguageContext.Provider
      value={{
        languageSpoken,
        languageLearned,
        setLanguageSpoken,
        setLanguageLearned,
      }}
    >
      {children}
    </LanguageContext.Provider>
  );
};

export const useLanguage = () => {
  const context = useContext(LanguageContext);
  if (!context) {
    throw new Error('useLanguage must be used within a LanguageProvider');
  }
  return context;
};
