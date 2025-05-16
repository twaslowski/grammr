import React, { useState } from 'react';

interface TranslationFormProps {
  onTranslate: (inputText: string) => void;
  isLoading: boolean;
}

const TranslationForm: React.FC<TranslationFormProps> = ({ onTranslate, isLoading }) => {
  const [inputText, setInputText] = useState('');

  const handleSubmit = () => {
    if (inputText.trim()) {
      onTranslate(inputText);
    }
  };

  return (
    <div className='space-y-4'>
      <textarea
        value={inputText}
        onChange={(e) => setInputText(e.target.value)}
        className='w-full p-4 min-h-[100px] border border-gray-300 dark:border-gray-700 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100'
        placeholder='Enter text to translate...'
      />
      <button
        onClick={handleSubmit}
        disabled={isLoading}
        className='w-full bg-primary-600 hover:bg-primary-700 text-white rounded py-2'
      >
        {isLoading ? 'Translating...' : 'Translate & Analyze'}
      </button>
    </div>
  );
};

export default TranslationForm;
