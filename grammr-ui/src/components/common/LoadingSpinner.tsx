import React from 'react';

export default function LoadingSpinner({ message = 'Loading...', size = 12 }) {
  return (
    <div className='flex items-center justify-center'>
      <div
        className={`animate-spin rounded-full h-${size} w-${size} border-t-2 border-b-2 border-blue-500`}
      ></div>
      <span className='ml-3 text-gray-700'>{message}</span>
    </div>
  );
}
