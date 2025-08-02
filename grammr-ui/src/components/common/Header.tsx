'use client';

import React, { useState } from 'react';
import { MobileSidebar } from '@/components/common/MobileSidebar';
import { DesktopSidebar } from '@/components/common/DesktopSidebar';

export const Header: React.FC<{ menuOpen: boolean; setMenuOpen: (open: boolean) => void }> = ({
  menuOpen,
  setMenuOpen,
}) => {
  return (
    <>
      <MobileSidebar />
      <DesktopSidebar menuOpen={menuOpen} setMenuOpen={setMenuOpen} />
    </>
  );
};
