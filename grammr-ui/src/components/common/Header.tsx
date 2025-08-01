'use client';

import React from 'react';
import { MobileSidebar } from '@/components/common/MobileSidebar';
import { DesktopSidebar } from '@/components/common/DesktopSidebar';

export const Header: React.FC = () => {
  return (
    <>
      <MobileSidebar />
      <DesktopSidebar />
    </>
  );
};
