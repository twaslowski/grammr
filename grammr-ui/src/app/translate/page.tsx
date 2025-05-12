'use client';

import { Info } from 'lucide-react';
import React, { useState } from 'react';

import TTSPlayer from '@/components/buttons/TextToSpeech';
import GenericFlashcardExport from '@/components/GenericFlashcardExport';
import Sidebar from '@/components/Sidebar';
import Token from '@/components/Token';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useLanguage } from '@/context/LanguageContext';
import { getLanguageName } from '@/lib/utils';
import Analysis from '@/types/analysis';
import TokenType from '@/types/tokenType';

const TranslatePage = () => {
  const { languageSpoken, languageLearned } = useLanguage();
  const [inputText, setInputText] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [translationData, setTranslationData] = useState<Analysis | null>(null);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [selectedToken, setSelectedToken] = useState<TokenType | null>(null);

  const handleTokenShare = (token: TokenType) => {
    setSelectedToken(token);
    setIsSidebarOpen(true);
  };

  const handleCloseSidebar = () => {
    setIsSidebarOpen(false);
    setSelectedToken(null);
  };

  const handleTranslation = async () => {
    if (!inputText.trim()) {
      setError('Please enter some text to translate');
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      const url = '/api/v1/translation';
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          phrase: inputText,
          userLanguageSpoken: languageSpoken,
          userLanguageLearned: languageLearned,
          performSemanticTranslation: true,
        }),
      });

      if (!response.ok) {
        throw new Error('Translation failed');
      }

      const data: Analysis = await response.json();
      setTranslationData(data);
    } catch (err) {
      console.warn(err);
      setError('Something went wrong. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <main>
      {selectedToken && (
        <Sidebar
          context={translationData?.semanticTranslation.translatedPhrase || ''}
          isOpen={isSidebarOpen}
          onClose={() => handleCloseSidebar()}
          token={selectedToken}
          languageCode={languageLearned}
        />
      )}

      <div className='container mx-auto px-4 py-8'>
        <Card className='w-full max-w-2xl mx-auto bg-white dark:bg-gray-800 shadow-md'>
          <CardHeader>
            <CardTitle className='flex items-center'>
              Translate & Learn
              <div className='group inline-block ml-2'>
                <Info className='w-4 h-4 text-gray-400 hover:text-gray-600 dark:text-gray-500 dark:hover:text-gray-400 cursor-pointer' />
                <div className='absolute hidden group-hover:block bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 p-2 rounded-lg shadow-lg text-sm text-gray-600 dark:text-gray-300 w-64 z-10'>
                  Enter text in your native language to translate it into the
                  language you're learning and get a detailed grammatical
                  analysis.
                </div>
              </div>
            </CardTitle>
          </CardHeader>
          <CardContent>
            {/* Input Section */}
            <div className='space-y-4'>
              {/* Text Input */}
              <textarea
                value={inputText}
                onChange={(e) => setInputText(e.target.value)}
                className='w-full p-4 min-h-[100px] border border-gray-300 dark:border-gray-700 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100'
                placeholder={`Enter a text in ${getLanguageName(
                  languageSpoken,
                )}...`}
              />

              {/* Translate Button */}
              <button
                onClick={handleTranslation}
                disabled={isLoading}
                className='w-full bg-primary-600 hover:bg-primary-700 text-white rounded py-2'
              >
                {isLoading ? 'Translating...' : 'Translate & Analyze'}
              </button>

              {/* Error Message */}
              {error && (
                <Alert variant='destructive'>
                  <AlertDescription>{error}</AlertDescription>
                </Alert>
              )}
            </div>

            {/* Results Section */}
            {translationData && (
              <div className='mt-8 space-y-6'>
                <Card className='bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700'>
                  <CardHeader>
                    <CardTitle className='text-lg text-gray-900 dark:text-gray-100 flex justify-between items-center'>
                      Translation
                      <TTSPlayer
                        text={
                          translationData.semanticTranslation.translatedPhrase
                        }
                      />
                      <GenericFlashcardExport
                        layout='p-4 text-sm h-6'
                        front={inputText}
                        back={
                          translationData.semanticTranslation.translatedPhrase
                        }
                      />
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className='flex flex-wrap gap-2'>
                      {translationData.analyzedTokens.map((token) => (
                        <Token
                          context={
                            translationData.semanticTranslation.translatedPhrase
                          }
                          key={token.index}
                          token={token}
                          onShare={() => handleTokenShare(token)}
                        />
                      ))}
                    </div>
                  </CardContent>
                </Card>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </main>
  );
};

export default TranslatePage;
