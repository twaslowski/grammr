'use client';

import React from 'react';
import { Header } from '@/components/common/Header';

export default function LayoutShell({ children }: { children: React.ReactNode }) {
  const [menuOpen, setMenuOpen] = React.useState(true);

  return (
    <div className='flex flex-col md:flex-row min-h-screen w-full'>
      <Header menuOpen={menuOpen} setMenuOpen={setMenuOpen} />
      <main
        className={
          menuOpen
            ? 'flex-grow transition-all duration-300 md:ml-[20vw]'
            : 'flex-grow transition-all duration-300 md:ml-0'
        }
      >
        {children}
      </main>
    </div>
  );
}
