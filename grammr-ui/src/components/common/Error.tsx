import React from 'react';
import Image from 'next/image';

interface ErrorProps {
  title?: string;
  message: string;
}

const Error: React.FC<ErrorProps> = ({ title = 'Oops!', message }) => {
  return (
    <div className='min-h-screen flex flex-col items-center justify-center bg-gray-50 px-4'>
      <div className='max-w-md w-full bg-white rounded-lg shadow-md p-8 text-center'>
        <Image
          src='/images/mascot_sad.png'
          alt='Error Mascot'
          height={72}
          width={72}
        />
        <h1 className='text-2xl font-bold text-gray-900 mb-2'>{title}</h1>
        <div className='w-16 h-1 bg-red-500 mx-auto mb-4'></div>
        <p className='text-gray-600'>{message}</p>
      </div>
    </div>
  );
};

export default Error;
