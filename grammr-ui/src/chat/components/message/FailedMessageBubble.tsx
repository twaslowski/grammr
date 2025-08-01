import React from 'react';
import clsx from 'clsx';

export const AssistantFailedMessageBubble: React.FC = () => {
  return (
    <div className={clsx('my-2 flex justify-start')}>
      <div className={clsx('rounded-2xl px-4 py-2 max-w-[75%] text-gray-800 bg-red-100')}>
        <p className='text-sm text-red-800'>An error occurred. Please try again later.</p>
      </div>
    </div>
  );
};
