// grammr-ui/src/chat/components/Suggestions.tsx
import React from 'react';

type SuggestionsProps = {
  onSuggestionClick: (text: string) => void;
};

const suggestions = [
  'How do I order food in Spanish?',
  'Tell me a fun fact in French.',
  "How do you say 'hello' in Japanese?",
  "Let's practice greetings.",
  'Teach me travel phrases.',
];

export const Suggestions: React.FC<SuggestionsProps> = ({ onSuggestionClick }) => {
  return (
    <div className='flex flex-wrap gap-2 justify-center mb-4'>
      {suggestions.map((text, idx) => (
        <button
          key={idx}
          onClick={() => onSuggestionClick(text)}
          className='text-xs bg-gray-100 hover:bg-gray-200 text-gray-800 px-3 py-1.5 rounded-full transition-colors border shadow-sm'
        >
          {text}
        </button>
      ))}
    </div>
  );
};
