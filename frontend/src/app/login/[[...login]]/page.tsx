'use client';
import { SignIn } from '@clerk/nextjs';
import React from 'react';

const LoginForm = () => {
  return (
    <div className='flex justify-center items-center min-h-screen'>
      <SignIn />
    </div>
  );
};

export default LoginForm;
