'use client';

import { Loader2 } from 'lucide-react';
import React, { useEffect, useRef, useState } from 'react';

import { useLanguage } from '@/context/LanguageContext';
import TokenType from '@/token/types/tokenType';
import { TokenTranslation } from '@/types';
import { useApi } from '@/hooks/useApi';

interface TranslationProps {
  context: string;
  token: TokenType;
  onTranslationLoaded: (translation: TokenTranslation) => void;
}

const Translation: React.FC<TranslationProps> = ({ context, token, onTranslationLoaded }) => {
  const { languageSpoken } = useLanguage();
  const { request, isLoading, error } = useApi();
  const onTranslationLoadedRef = useRef(onTranslationLoaded);
  const [data, setData] = useState<TokenTranslation | null>(null);

  useEffect(() => {
    onTranslationLoadedRef.current = onTranslationLoaded;
  }, [onTranslationLoaded]);

  useEffect(() => {
    const fetchTranslation = async (): Promise<TokenTranslation> => {
      const data = await request<TokenTranslation>('/api/v2/translations/word', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          context: context,
          source: token.text,
          targetLanguage: languageSpoken,
        }),
      });
      setData(data);
      return data;
    };

    if (!token.translation) {
      fetchTranslation()
        .then((res) => onTranslationLoaded(res))
        .catch((err) => console.error(err));
    }
  }, [context, token, onTranslationLoaded]);

  return (
    <div>
      {isLoading ? (
        <div className='flex items-center text-gray-500'>
          <Loader2 className='h-4 w-4 animate-spin mr-2' />
          Loading translation...
        </div>
      ) : error ? (
        <div className='text-red-500 text-sm bg-red-50 rounded py-1 mt-1 p-2'>
          Error loading translation
        </div>
      ) : data ? (
        <p>Translation: {data.translation}</p>
      ) : null}
    </div>
  );
};

export default Translation;
