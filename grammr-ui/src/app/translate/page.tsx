'use client';

import React, { useState } from 'react';
import TranslationCard from '@/components/language/TranslationCard';
import { useLanguage } from '@/context/LanguageContext';
import { InputArea } from '@/components/common/InputArea';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import { useApi } from '@/hooks/useApi';
import Error from '@/components/common/Error';
import { Translation } from '@/types/translation';

const TranslatePage = () => {
  const { languageSpoken, languageLearned } = useLanguage();
  const { request, isLoading, error } = useApi();
  const [translation, setTranslation] = useState<Translation | null>();

  const handleTranslation = async (inputText: string) => {
    try {
      const data = await request<Translation>('/api/v2/translations/phrase', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          phrase: inputText,
          sourceLanguage: languageSpoken,
          targetLanguage: languageLearned,
          performAnalysis: true,
        }),
      });
      setTranslation(data);
    } catch (err) {
      // Handled by useApi hook
    }
  };

  return (
    <main>
      <div className='px-4 py-8 max-w-3xl mx-auto'>
        <InputArea onEnter={handleTranslation} clear={false} />

        {isLoading && <LoadingSpinner size={24} />}
        {error && (
          <div className='mt-4 p-4'>
            <Error title={'Something went wrong'} message={error.message} />
          </div>
        )}

        {translation && (
          <div className='mt-8 space-y-6 max-w-3xl mx-auto'>
            <TranslationCard translation={translation} />
          </div>
        )}
      </div>
    </main>
  );
};

export default TranslatePage;
