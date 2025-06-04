import React, { useState } from 'react';
import { SendHorizonal } from 'lucide-react';

export const InputArea: React.FC<{ onEnter: (input: string) => void, clear: boolean }> = ({ onEnter, clear }) => {
  const [input, setInput] = useState('');

  const handleSubmit = () => {
    if (input.trim()) {
      onEnter(input);
    }
    if (clear) {
      setInput('');
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      void handleSubmit();
    }
  };

  return (
    <div className='w-full border border-gray-300 rounded-2xl px-4 resize-none shadow-sm flex flex-col py-2'>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          void handleSubmit();
        }}
      >
        <textarea
          className='border-none w-full resize-none focus:outline-none focus:ring-0'
          placeholder='Enter text to translate...'
          rows={1}
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyPress}
        />
      </form>
      <button
        type='submit'
        aria-label='Send message'
        className='self-end p-2 rounded-full bg-black text-white hover:bg-gray-800 transition-colors shadow-md'
        onClick={() => void handleSubmit()}
      >
        <SendHorizonal className='w-5 h-5' />
      </button>
    </div>
  );
};
