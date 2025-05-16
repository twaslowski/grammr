import { Loader2 } from 'lucide-react';
import React, { useEffect, useRef, useState } from 'react';

import { useLanguage } from '@/context/LanguageContext';
import { fetchTranslation } from '@/lib/translation';
import { TokenTranslation } from '@/types';
import TokenType from '@/types/tokenType';

interface TranslationProps {
  context: string;
  token: TokenType;
  onTranslationLoaded: (translation: TokenTranslation) => void;
}

const Translation: React.FC<TranslationProps> = ({
  context,
  token,
  onTranslationLoaded,
}) => {
  const { languageSpoken, languageLearned } = useLanguage();
  const [isTranslationLoading, setIsTranslationLoading] = useState(false);
  const [translationError, setTranslationError] = useState<string | null>(null);
  const [translationData, setTranslationData] = useState<{
    translation: string;
    source: string;
  } | null>(null);

  const onTranslationLoadedRef = useRef(onTranslationLoaded);

  useEffect(() => {
    onTranslationLoadedRef.current = onTranslationLoaded;
  }, [onTranslationLoaded]);

  useEffect(() => {
    const loadTranslation = async () => {
      if (!token || token.translation) return;
      console.log('Loading translation for', token.text);

      setIsTranslationLoading(true);
      setTranslationError(null);

      try {
        const translationResult = await fetchTranslation(
          context,
          token.text,
          languageSpoken,
        );
        setTranslationData(translationResult);
        onTranslationLoadedRef.current(translationResult);
      } catch (err) {
        if (err instanceof Error) setTranslationError(err.message);
        else setTranslationError('Unknown error when loading translation');
      } finally {
        setIsTranslationLoading(false);
      }
    };

    loadTranslation();
  }, [context, token, languageSpoken]);

  const translation = token.translation || translationData;

  return (
    <div>
      {isTranslationLoading ? (
        <div className='flex items-center text-gray-500'>
          <Loader2 className='h-4 w-4 animate-spin mr-2' />
          Loading translation...
        </div>
      ) : translationError ? (
        <div className='text-red-500 text-sm bg-red-50 rounded py-1 mt-1 p-2'>
          Error loading translation
        </div>
      ) : translation ? (
        <p>Translation: {translation.translation}</p>
      ) : null}
    </div>
  );
};

export default Translation;
