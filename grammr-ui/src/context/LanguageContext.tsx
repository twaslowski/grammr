"use client";

import {createContext, ReactNode, useContext, useEffect, useState} from "react";

type LanguageContextProps = {
  languageSpoken: string;
  languageLearned: string;
  setLanguageSpoken: (lang: string) => void;
  setLanguageLearned: (lang: string) => void;
  isReady: boolean;
};

const LanguageContext = createContext<LanguageContextProps | undefined>(undefined);

export function LanguageProvider({
                                   children,
                                 }: {
  children: ReactNode;
}) {
  const [languageSpoken, setLanguageSpokenState] = useState('');
  const [languageLearned, setLanguageLearnedState] = useState('');
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    const savedSpoken = localStorage.getItem("languageSpoken");
    const savedLearned = localStorage.getItem("languageLearned");

    if (savedSpoken) setLanguageSpokenState(savedSpoken);
    if (savedLearned) setLanguageLearnedState(savedLearned);

    setIsReady(true);
  }, []);

  const setLanguageSpoken = (lang: string) => {
    localStorage.setItem("languageSpoken", lang);
    setLanguageSpokenState(lang);
  };

  const setLanguageLearned = (lang: string) => {
    localStorage.setItem("languageLearned", lang);
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
  if (!context) throw new Error("useLanguage must be used within a LanguageProvider");
  return context;
}
