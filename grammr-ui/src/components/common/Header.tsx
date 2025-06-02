'use client';

import React from 'react';
import { MobileHeader } from '@/components/common/MobileHeader';
import { DesktopHeader } from '@/components/common/DesktopHeader';

export const Header: React.FC = () => {
  return (
    <>
      <DesktopHeader />
      <MobileHeader />
    </>
  );
};
