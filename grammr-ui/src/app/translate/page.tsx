'use client';

import React, { useState } from 'react';
import TranslationCard from '@/components/language/TranslationCard';
import { useLanguage } from '@/context/LanguageContext';
import { InputArea } from '@/components/common/InputArea';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import { useApi } from '@/hooks/useApi';
import Error from '@/components/common/Error';
import { Translation } from '@/types/translation';
import { AnalysisV2 } from '@/types/analysis';

const TranslatePage = () => {
  const { languageSpoken, languageLearned } = useLanguage();
  const { request, isLoading, error } = useApi();
  const [translation, setTranslation] = useState<Translation | null>();
  // Default false represents translations; true represents analysis
  const [toggleDirection, setToggleDirection] = useState<boolean>(false);

  const handleSubmit = async (inputText: string) => {
    if (toggleDirection) {
      await handleAnalysis(inputText);
    } else {
      await handleTranslation(inputText);
    }
  };

  const handleTranslation = async (inputText: string) => {
    setTranslation(null);
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

  const handleAnalysis = async (phrase: string) => {
    setTranslation(null);
    try {
      const [analysisResult, translationResult] = await Promise.all([
        request<AnalysisV2>('/api/v2/analysis', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            phrase: phrase,
            language: languageLearned,
          }),
        }),
        request<Translation>('/api/v2/translations/phrase', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            phrase: phrase,
            sourceLanguage: languageLearned,
            targetLanguage: languageSpoken,
            performAnalysis: false,
          }),
        }),
      ]);

      // Set such that the flashcard export would show the foreign language phrase as front
      setTranslation({
        translation: translationResult.source,
        source: translationResult.translation,
        sourceLanguage: languageLearned,
        targetLanguage: languageSpoken,
        analysis: analysisResult,
      });
    } catch (err) {
      // Handled by useApi hook - if either request fails, translation won't be set
    }
  };

  return (
    <main>
      <div className='px-4 py-8 max-w-3xl mx-auto'>
        <InputArea onEnter={handleSubmit} clear={false} />

        {/* Translation Direction Button */}
        <div className='mt-4 flex justify-center'>
          <button
            onClick={() => setToggleDirection(!toggleDirection)}
            className='flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors'
          >
            <span>{toggleDirection ? languageLearned : languageSpoken}</span>
            <span>→</span>
            <span>{toggleDirection ? languageSpoken : languageLearned}</span>
            <svg className='w-4 h-4 ml-2' fill='none' stroke='currentColor' viewBox='0 0 24 24'>
              <path
                strokeLinecap='round'
                strokeLinejoin='round'
                strokeWidth={2}
                d='M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4'
              />
            </svg>
          </button>
        </div>

        {isLoading && <LoadingSpinner size={24} />}
        {error && (
          <div className='mt-4 p-4'>
            <Error title={'Something went wrong'}>{error.message}</Error>
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
