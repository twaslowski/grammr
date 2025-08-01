'use client';

import React, { useEffect, useState } from 'react';

type SuggestionsProps = {
  onSuggestionClickAction: (text: string) => void;
  numberOfSuggestions?: number;
};

const suggestions = [
  'How do I introduce myself?',
  'Can you help me with directions vocabulary?',
  'What should I say when meeting someone new?',
  'Let’s talk about weather phrases.',
  'Teach me how to order coffee.',
  'Let’s practice small talk.',
  'What do I say when I don’t understand?',
  'Let’s learn phrases for public transportation.',
  'How do I compliment someone politely?',
];

const getRandomSuggestions = (arr: string[], n: number) => {
  const shuffled = [...arr].sort(() => 0.5 - Math.random());
  return shuffled.slice(0, n);
};

export const Suggestions: React.FC<SuggestionsProps> = ({
  onSuggestionClickAction,
  numberOfSuggestions = 3,
}) => {
  const [sampledSuggestions, setSampledSuggestions] = useState<string[]>([]);

  useEffect(() => {
    const randomSuggestions = getRandomSuggestions(suggestions, numberOfSuggestions);
    setSampledSuggestions(randomSuggestions);
  }, [numberOfSuggestions]);

  return (
    <div className='flex flex-wrap gap-2 justify-center mb-4'>
      {sampledSuggestions.map((text, idx) => (
        <button
          key={idx}
          onClick={() => onSuggestionClickAction(text)}
          className='text-xs bg-gray-100 hover:bg-gray-200 text-gray-800 px-3 py-1.5 rounded-full transition-colors border shadow-sm'
        >
          {text}
        </button>
      ))}
    </div>
  );
};
