import React from 'react';
import clsx from 'clsx';
import { TypingDots } from '@/chat/components/TypingDots';

export const LoadingMessageBubble: React.FC = () => {
  return (
    <div className={clsx('my-2 flex justify-start')}>
      <div
        className={clsx('rounded-2xl px-4 py-2 max-w-[75%] text-gray-800 bg-white animate-pulse')}
      >
        <TypingDots />
      </div>
    </div>
  );
};
