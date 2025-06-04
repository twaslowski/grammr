'use client';

import React, { useState } from 'react';
import AnalysisCard from '@/components/language/AnalysisCard';
import { useLanguage } from '@/context/LanguageContext';
import useAnalysis from '@/hooks/useAnalysis';
import { InputArea } from '@/components/common/InputArea';
import LoadingSpinner from '@/components/common/LoadingSpinner';

const TranslatePage = () => {
  const { languageSpoken, languageLearned } = useLanguage();
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const { analysis, saveAnalysis, clearAnalysis } = useAnalysis();

  const handleTranslation = async (inputText: string) => {
    setIsLoading(true);
    setError('');
    clearAnalysis();

    try {
      const response = await fetch('/api/v1/translation', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          phrase: inputText,
          userLanguageSpoken: languageSpoken,
          userLanguageLearned: languageLearned,
          performSemanticTranslation: true,
        }),
      });

      const data = await response.json();
      saveAnalysis(data);
    } catch (err) {
      setError('Something went wrong. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <main>
      <div className='px-4 py-8 max-w-3xl mx-auto'>
        <InputArea onEnter={handleTranslation} clear={false} />

        {isLoading && <LoadingSpinner size={24} spinnerColor='black' />}
        {error && <div className='text-red-500 text-sm p-3 bg-red-50 rounded py-4'>{error}</div>}

        {analysis && (
          <div className='mt-8 space-y-6 max-w-3xl mx-auto'>
            <AnalysisCard analysis={analysis} />
          </div>
        )}
      </div>
    </main>
  );
};

export default TranslatePage;
