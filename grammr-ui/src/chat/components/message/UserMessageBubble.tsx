import React from 'react';
import { ChatMessage } from '@/chat/types/message';
import clsx from 'clsx';

export const UserMessageBubble: React.FC<{ message: ChatMessage }> = ({ message }) => {
  return (
    <div className={clsx('my-2 flex justify-end')}>
      <div className={clsx('rounded-2xl px-4 py-2 max-w-[75%] text-gray-800 bg-gray-200')}>
        <p className='whitespace-pre-wrap break-words'>{message.content}</p>
      </div>
    </div>
  );
};
