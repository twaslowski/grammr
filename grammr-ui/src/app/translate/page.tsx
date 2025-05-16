'use client';

import React, { useState } from 'react';
import Sidebar from '@/components/Sidebar';
import AnalysisCard from './AnalysisCard';
import TranslationForm from './TranslationForm';
import { useLanguage } from '@/context/LanguageContext';
import useAnalysis from '@/hooks/useAnalysis';
import TokenType from '@/types/tokenType';

const TranslatePage = () => {
  const { languageSpoken, languageLearned } = useLanguage();
  const [selectedToken, setSelectedToken] = useState<TokenType | null>(null);
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
      {selectedToken && (
        <Sidebar
          context={analysis?.semanticTranslation.translatedPhrase || ''}
          onClose={() => setSelectedToken(null)}
          token={selectedToken}
          languageCode={languageLearned}
        />
      )}

      <div className='px-4 py-8 max-w-3xl mx-auto'>
        <TranslationForm
          onTranslate={handleTranslation}
          isLoading={isLoading}
        />

        {error && (
          <div className='text-red-500 text-sm p-3 bg-red-50 rounded py-4'>
            {error}
          </div>
        )}

        {analysis && (
          <div className='mt-8 space-y-6 max-w-3xl mx-auto'>
            <AnalysisCard
              analysis={analysis}
              onTokenClick={(token) => setSelectedToken(token)}
            />
          </div>
        )}
      </div>
    </main>
  );
};

export default TranslatePage;
