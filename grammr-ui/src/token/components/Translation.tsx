import { Loader2 } from 'lucide-react';
import React, { useEffect, useRef } from 'react';

import { useLanguage } from '@/context/LanguageContext';
import { useTranslation } from '@/hooks/useTranslation';
import TokenType from '@/token/types/tokenType';
import { TokenTranslation } from '@/types';

interface TranslationProps {
  context: string;
  token: TokenType;
  onTranslationLoaded: (translation: TokenTranslation) => void;
}

const Translation: React.FC<TranslationProps> = ({ context, token, onTranslationLoaded }) => {
  const { languageSpoken } = useLanguage();
  const onTranslationLoadedRef = useRef(onTranslationLoaded);

  useEffect(() => {
    onTranslationLoadedRef.current = onTranslationLoaded;
  }, [onTranslationLoaded]);

  const {
    data: translation,
    loading: isTranslationLoading,
    error: translationError,
  } = useTranslation(context, token.text, languageSpoken, !token.translation);

  useEffect(() => {
    if (translation) {
      onTranslationLoadedRef.current(translation);
    }
  }, [translation]);

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
