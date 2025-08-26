import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { ChevronDown } from 'lucide-react';
import { languages } from '@/constant/languages';
import React, { useEffect } from 'react';
import { useLanguage } from '@/context/LanguageContext';

export const LanguageSelectionDropdown: React.FC = () => {
  const { languageSpoken, languageLearned, setLanguageSpoken, setLanguageLearned } = useLanguage();

  useEffect(() => {
    const savedSpoken = localStorage.getItem('languageSpoken');
    const savedLearned = localStorage.getItem('languageLearned');
    if (savedSpoken) setLanguageSpoken(savedSpoken);
    if (savedLearned) setLanguageLearned(savedLearned);
  }, [setLanguageSpoken, setLanguageLearned]);

  const updateLanguages = (spoken: string, learned: string) => {
    // Prevent setting the same language for both speaking and learning
    if (spoken === learned) {
      return; // Early return if trying to set the same language
    }

    setLanguageSpoken(spoken);
    setLanguageLearned(learned);
    localStorage.setItem('languageSpoken', spoken);
    localStorage.setItem('languageLearned', learned);
  };

  const getLanguageByCode = (code: string) => {
    return languages.find((lang) => lang.code === code) || languages[0];
  };

  const spokenLanguage = getLanguageByCode(languageSpoken);
  const learnedLanguage = getLanguageByCode(languageLearned);

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
                  languageSpoken === lang.code ? 'bg-blue-100 text-blue-700' : 
                  lang.code === languageLearned ? 'bg-gray-200 text-gray-400 cursor-not-allowed' :
                  'hover:bg-gray-100'
                }`}
                onClick={() => {
                  if (lang.code !== languageLearned) {
                    updateLanguages(lang.code, languageLearned);
                  }
                }}
                disabled={lang.code === languageLearned}
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
            {languages.filter((l) => l.learnable).map((lang) => (
              <button
                key={`learn-${lang.code}`}
                className={`flex items-center px-2 py-2 text-sm rounded-lg ${
                  languageLearned === lang.code ? 'bg-blue-100 text-blue-700' : 
                  lang.code === languageSpoken ? 'bg-gray-200 text-gray-400 cursor-not-allowed' :
                  'hover:bg-gray-100'
                }`}
                onClick={() => {
                  if (lang.code !== languageSpoken) {
                    updateLanguages(languageSpoken, lang.code);
                  }
                }}
                disabled={lang.code === languageSpoken}
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
