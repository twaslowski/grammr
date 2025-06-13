'use client';

import React from 'react';
import AnalysisCard from '@/components/language/AnalysisCard';
import {useLanguage} from '@/context/LanguageContext';
import useAnalysis from '@/hooks/useAnalysis';
import {InputArea} from '@/components/common/InputArea';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import {AnalysisV2} from '@/types/analysis';
import {useApi} from '@/hooks/useApi';
import Error from "@/components/common/Error";

const TranslatePage = () => {
  const {languageSpoken, languageLearned} = useLanguage();
  const {request, isLoading, error} = useApi();
  const {analysis, saveAnalysis, clearAnalysis} = useAnalysis();

  const handleTranslation = async (inputText: string) => {
    clearAnalysis();

    try {
      const data = await request<AnalysisV2>('/api/v2/translations/phrase', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
          phrase: inputText,
          sourceLanguage: languageSpoken,
          targetLanguage: languageLearned,
          performAnalysis: true,
        }),
      });

      saveAnalysis(data);
    } catch (err) {
      // Handled by useApi hook
    }
  };

  return (
      <main>
        <div className='px-4 py-8 max-w-3xl mx-auto'>
          <InputArea onEnter={handleTranslation} clear={false}/>

          {isLoading && <LoadingSpinner size={24}/>}
          {error && (
              <div className='mt-4 p-4'>
                <Error title={'Something went wrong'} message={error.message}/>
              </div>
          )}

          {analysis && (
              <div className='mt-8 space-y-6 max-w-3xl mx-auto'>
                <AnalysisCard analysis={analysis}/>
              </div>
          )}
        </div>
      </main>
  );
};

export default TranslatePage;
