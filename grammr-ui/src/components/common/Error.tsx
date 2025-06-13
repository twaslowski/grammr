import React from 'react';
import Image from 'next/image';

interface ErrorProps {
  title?: string;
  message: string;
}

const Error: React.FC<ErrorProps> = ({title = 'Oops!', message}) => {
  return (
      <div className='flex flex-col items-center justify-center px-4'>
        <Image
            src='/images/mascot_sad.png'
            alt='Error Mascot'
            height={96}
            width={96}
            className='pb-3'
        />
        <h1 className='text-2xl font-bold text-gray-900 mb-2'>{title}</h1>
        <div className='w-16 h-1 bg-red-500 mx-auto mb-4'></div>
        <p className='text-gray-600'>{message}</p>
      </div>
  );
};

export default Error;
