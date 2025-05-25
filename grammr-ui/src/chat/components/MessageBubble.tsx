import React from 'react';
import { ChatMessage } from '@/chat/types/message';
import clsx from 'clsx';

export const MessageBubble: React.FC<{ message: ChatMessage }> = ({ message }) => {
  const isUser = message.role === 'user';
  const isAssistant = message.role === 'assistant';

  return (
    <div className={clsx('my-2 flex', isUser ? 'justify-end' : 'justify-start')}>
      <div
        className={clsx(
          'rounded-2xl px-4 py-2 max-w-[75%] text-gray-800',
          isUser && 'bg-gray-200',
          isAssistant && 'bg-white',
          message.status === 'streaming' && 'animate-pulse',
        )}
      >
        <p className='whitespace-pre-wrap break-words'>{message.content}</p>
        {message.status === 'failed' && <p className='text-sm text-red-500 mt-1'>Failed to send</p>}
      </div>
    </div>
  );
};
