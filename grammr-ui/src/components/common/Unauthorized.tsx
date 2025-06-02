import Link from 'next/link';
import React from 'react';

const Unauthorized = () => {
  return (
    <div className='min-h-screen flex items-center justify-center px-4'>
      <div className='max-w-md w-full bg-white rounded-lg shadow-md p-8'>
        <div className='text-center'>
          <h1 className='text-2xl font-bold text-gray-900 mb-2'>Signed Out</h1>
          <div className='w-16 h-1 bg-red-500 mx-auto mb-4'></div>
          <p className='text-gray-600 mb-6'>
            This page requires you to be signed in to your account. Please create an account or sign
            in to continue.
          </p>

          <div className='flex flex-col space-y-4'>
            <Link
              href='/login'
              className='w-full inline-flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500'
            >
              Sign in
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Unauthorized;
