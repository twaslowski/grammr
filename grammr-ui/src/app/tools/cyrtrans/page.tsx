'use client';

import React, { useState } from 'react';
import { InputArea } from '@/components/common/InputArea';
import cyrillicToTranslit from 'cyrillic-to-translit-js';
import { CopyIcon } from 'lucide-react';

const TransliterationPage = () => {
  const [transliteration, setTransliteration] = useState<string>('');
  const [copyFeedback, setCopyFeedback] = useState<boolean>(false);

  const transliterate = (original: string): void => {
    console.log(original);
    setTransliteration(cyrillicToTranslit().reverse(original));
  };

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(transliteration);
      setCopyFeedback(true);
      setTimeout(() => setCopyFeedback(false), 2000);
    } catch (err) {
      console.error('Failed to copy text: ', err);
    }
  };

  return (
    <main>
      <div className='px-4 py-8 max-w-3xl mx-auto'>
        <InputArea onEnter={() => {}} onChange={transliterate} clear={false} />

        <div className='w-full border border-gray-300 rounded-2xl px-4 resize-none shadow-sm flex flex-row items-start py-2 mt-4'>
          <textarea
            className='border-none w-full resize-none focus:outline-none focus:ring-0'
            rows={1}
            placeholder={"Начни печатать!"}
            value={transliteration}
            readOnly={true}
          />

          <div className='ml-2 flex flex-col items-center'>
            <button
              onClick={handleCopy}
              className='p-2 text-gray-500 hover:text-gray-700 transition-colors'
              title='Copy to clipboard'
            >
              <CopyIcon />
            </button>
            {copyFeedback && (
              <span className='text-xs text-green-600 mt-1 animate-fade-in'>Copied!</span>
            )}
          </div>
        </div>
      </div>
    </main>
  );
};

export default TransliterationPage;
