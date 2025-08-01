import React from 'react';

export default function LoadingSpinner({
  message = 'Loading...',
  size = 12,
  textColor = 'text-gray-700',
  spinnerColor = 'border-blue-500',
}) {
  return (
    <div className='flex items-center justify-center'>
      <div
        className={`animate-spin rounded-full h-${size} w-${size} border-t-2 border-b-2 ${spinnerColor}`}
      ></div>
      <span className={`ml-3 ${textColor}`}>{message}</span>
    </div>
  );
}
