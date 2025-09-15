import Link from 'next/link';
import React from 'react';
import { LockIcon } from '@/components/ui/icons';
import { LogInIcon } from 'lucide-react';

const Unauthorized = () => {
  return (
    <div className='min-h-screen flex items-center justify-center px-4'>
      <div className='max-w-md w-full bg-white rounded-2xl shadow-xl border border-gray-100 p-8 relative overflow-hidden'>
        <div className='absolute top-0 right-0 w-32 h-32 rounded-full -translate-y-16 translate-x-16'></div>
        <div className='absolute bottom-0 left-0 w-24 h-24 rounded-full translate-y-12 -translate-x-12'></div>

        <div className='text-center'>
          <div className='mx-auto w-16 h-16 bg-primary rounded-full flex items-center justify-center mb-6 shadow-lg text-white'>
            <LockIcon size={36} />
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
              className='w-full inline-flex justify-center items-center py-2 gap-x-2 rounded-xl shadow-lg font-medium text-white bg-primary-500 hover:bg-primary-600 transition-colors'
            >
              <LogInIcon size={16} />
              <p>Sign In to Continue</p>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Unauthorized;
