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
import { LucideArrowLeftRight, LucideArrowRight } from 'lucide-react';

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
          sourceLanguage: languageSpoken.code,
          targetLanguage: languageLearned.code,
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
            language: languageLearned.code,
          }),
        }),
        request<Translation>('/api/v2/translations/phrase', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            phrase: phrase,
            sourceLanguage: languageLearned.code,
            targetLanguage: languageSpoken.code,
            performAnalysis: false,
          }),
        }),
      ]);

      // Set such that the flashcard export would show the foreign language phrase as front
      setTranslation({
        translation: translationResult.translation,
        source: translationResult.source,
        sourceLanguage: languageLearned.code,
        targetLanguage: languageSpoken.code,
        analysis: analysisResult,
      });
    } catch (err) {
      // Handled by useApi hook - if either request fails, translation won't be set
    }
  };

  return (
    <main>
      <div className='px-4 py-8 max-w-3xl mx-auto'>
        {/* Page Title & Description */}
        <h1 className='text-3xl font-bold mb-2 text-center'>Phrase Translator</h1>
        <p className='text-gray-600 mb-4 text-center'>
          Enter a phrase to translate it and get detailed grammatical analysis.
        </p>

        {/* Target Language Indicator */}
        <div className='flex items-center justify-center mb-6'>
          <button
            onClick={() => setToggleDirection(!toggleDirection)}
            className='focus:outline-none'
          >
            <div className='inline-flex items-center px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 text-gray-700'>
              <span className='mx-2 font-semibold'>
                {toggleDirection ? languageLearned.name : languageSpoken.name}
              </span>
              <LucideArrowRight size={18} />
              <span className='mx-2 font-semibold'>
                {toggleDirection ? languageSpoken.name : languageLearned.name}
              </span>
              <LucideArrowLeftRight className='ml-2' size={18} />
            </div>
          </button>
        </div>

        <InputArea onEnter={handleSubmit} clear={false} />

        <div className='mt-8' />

        {isLoading && <LoadingSpinner size={24} />}
        {error && <Error title={'Something went wrong'}>{error.message}</Error>}

        {translation && (
          <div className='space-y-6 max-w-3xl mx-auto'>
            <TranslationCard translation={translation} />
          </div>
        )}
      </div>
    </main>
  );
};

export default TranslatePage;
