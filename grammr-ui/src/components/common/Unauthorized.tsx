import Link from 'next/link';
import React from 'react';

const Unauthorized = () => {
  return (
    <div className='min-h-screen flex items-center justify-center px-4'>
      <div className='max-w-md w-full bg-white rounded-2xl shadow-xl border border-gray-100 p-8 relative overflow-hidden'>
        <div className='absolute top-0 right-0 w-32 h-32 rounded-full -translate-y-16 translate-x-16'></div>
        <div className='absolute bottom-0 left-0 w-24 h-24 rounded-full translate-y-12 -translate-x-12'></div>

        <div className='text-center relative z-10'>
          {/* Icon */}
          <div className='mx-auto w-16 h-16 bg-gradient-to-r from-blue-500 to-indigo-600 rounded-full flex items-center justify-center mb-6 shadow-lg'>
            <svg
              className='w-8 h-8 text-white'
              fill='none'
              stroke='currentColor'
              viewBox='0 0 24 24'
            >
              <path
                strokeLinecap='round'
                strokeLinejoin='round'
                strokeWidth={2}
                d='M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z'
              />
            </svg>
          </div>

          <h1 className='text-3xl font-bold text-gray-900 mb-3'>Not Signed In</h1>
          <div className='w-20 h-1 mx-auto mb-6 rounded-full'></div>

          <p className='text-gray-600 mb-8 leading-relaxed'>
            You need to be signed in to access this page. Sign in or create an account to access all
            features of grammr.
          </p>

          <div className='space-y-4'>
            <Link
              href='/login'
              className='w-full inline-flex justify-center items-center py-3 px-6 border border-transparent rounded-xl shadow-lg text-base font-medium text-white bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all duration-200 transform hover:-translate-y-0.5 hover:shadow-xl'
            >
              <svg className='w-5 h-5 mr-2' fill='none' stroke='currentColor' viewBox='0 0 24 24'>
                <path
                  strokeLinecap='round'
                  strokeLinejoin='round'
                  strokeWidth={2}
                  d='M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1'
                />
              </svg>
              Sign In to Continue
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Unauthorized;
