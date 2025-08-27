import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { ChevronDown } from 'lucide-react';
import { Language, languages } from '@/constant/languages';
import React from 'react';
import { useLanguage } from '@/context/LanguageContext';

export const LanguageSelectionDropdown: React.FC = () => {
  const { languageSpoken, languageLearned, setLanguageSpoken, setLanguageLearned } = useLanguage();

  const updateLanguages = (spoken: Language, learned: Language) => {
    // Prevent setting the same language for both speaking and learning
    if (spoken.code === learned.code) {
      return;
    }
    setLanguageSpoken(spoken);
    setLanguageLearned(learned);
  };

  // spokenLanguage and learnedLanguage are already Language objects
  const spokenLanguage = languageSpoken;
  const learnedLanguage = languageLearned;

  return (
    <DropdownMenu>
      <DropdownMenuTrigger className='m-4 inline-flex items-center px-3 py-2 border border-gray-300 rounded-lg text-sm font-medium text-gray-700 bg-white hover:bg-gray-50'>
        <span className='mr-1'>{spokenLanguage.flag}</span>
        <span className='mx-1'>→</span>
        <span className='mr-1'>{learnedLanguage.flag}</span>
        <ChevronDown className='ml-1 w-4 h-4' />
      </DropdownMenuTrigger>
      <DropdownMenuContent className='w-full p-4 mt-2'>
        <div className='mb-4'>
          <p className='text-sm font-medium text-gray-700 mb-2'>I speak:</p>
          <div className='grid grid-cols-2 gap-2'>
            {languages.map((lang) => (
              <button
                key={`speak-${lang.code}`}
                className={`flex items-center px-2 py-2 text-sm rounded-lg ${
                  languageSpoken.code === lang.code
                    ? 'bg-blue-100 text-blue-700'
                    : lang.code === languageLearned.code
                      ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                      : 'hover:bg-gray-100'
                }`}
                onClick={() => {
                  if (lang.code !== languageLearned.code) {
                    updateLanguages(lang, languageLearned);
                  }
                }}
                disabled={lang.code === languageLearned.code}
              >
                <span className='mr-2'>{lang.flag}</span>
                <span>{lang.name}</span>
              </button>
            ))}
          </div>
        </div>
        <div>
          <p className='text-sm font-medium text-gray-700 mb-2'>I am learning:</p>
          <div className='grid grid-cols-2 gap-2'>
            {languages
              .filter((l) => l.learnable)
              .map((lang) => (
                <button
                  key={`learn-${lang.code}`}
                  className={`flex items-center px-2 py-2 text-sm rounded-lg ${
                    languageLearned.code === lang.code
                      ? 'bg-blue-100 text-blue-700'
                      : lang.code === languageSpoken.code
                        ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                        : 'hover:bg-gray-100'
                  }`}
                  onClick={() => {
                    if (lang.code !== languageSpoken.code) {
                      updateLanguages(languageSpoken, lang);
                    }
                  }}
                  disabled={lang.code === languageSpoken.code}
                >
                  <span className='mr-2'>{lang.flag}</span>
                  <span>{lang.name}</span>
                </button>
              ))}
          </div>
        </div>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};
