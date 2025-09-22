import React from 'react';
import clsx from 'clsx';
import { TypingDots } from '@/chat/components/TypingDots';
import Image from 'next/image';

export const LoadingMessageBubble: React.FC = () => {
  return (
    <div className='my-2 flex justify-start gap-2'>
      <div className='w-12 h-12 flex items-center justify-center rounded-full border'>
        <Image src={'/images/mascot.png'} alt={'mascot'} width={40} height={40} />
      </div>
      <div
        className={clsx(
          'rounded-2xl border border-gray-300 p-2 max-w-[75%] text-gray-800 bg-white',
        )}
      >
        <TypingDots />
      </div>
    </div>
  );
};
