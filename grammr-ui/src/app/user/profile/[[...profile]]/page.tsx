import { UserProfile } from '@clerk/nextjs';
import React from 'react';

export default function UserProfilePage() {
  return (
    <div className='mt-5 flex items-center justify-center'>
      <UserProfile />
    </div>
  );
}
