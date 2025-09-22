'use client';

import { UserProfile, useUser } from '@clerk/nextjs';
import React from 'react';
import Unauthorized from '@/components/common/Unauthorized';

export default function UserProfilePage() {
  const { isSignedIn } = useUser();

  if (!isSignedIn) {
    return <Unauthorized />;
  }

  return (
    <div className='mt-5 flex items-center justify-center'>
      <UserProfile />
    </div>
  );
}
