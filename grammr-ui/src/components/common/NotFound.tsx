import { ArrowLeft, Search } from 'lucide-react';
import Link from 'next/link';
import React from 'react';

export default function NotFound({ message = 'Page not found', backHref = '/' }) {
  return (
    <div className='flex flex-col items-center justify-center min-h-screen p-4 text-center'>
      <div className='bg-gray-100 rounded-full p-6 mb-4'>
        <Search size={48} className='text-gray-400' />
      </div>
      <h2 className='text-2xl font-semibold mb-3'>{message}</h2>
      <p className='text-gray-600 mb-6 max-w-md'>
        The resource you're looking for doesn't exist or has been removed.
      </p>
      <Link
        href={backHref}
        className='flex items-center px-4 py-2 bg-primary-500 text-white rounded-md hover:bg-primary-600 transition-colors'
      >
        <ArrowLeft size={16} className='mr-2' />
        Go Back
      </Link>
    </div>
  );
}
