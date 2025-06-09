import React, { useEffect, useState } from 'react';
import Image from 'next/image';
import { ChevronDown, Menu, X } from 'lucide-react';
import { ClerkLoading, SignedIn, SignedOut, SignInButton } from '@clerk/nextjs';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import UserMenu from '@/components/common/UserMenu';
import { languages } from '@/constant/languages';
import { useLanguage } from '@/context/LanguageContext';
import { useRouter } from 'next/navigation';
import { clsx } from 'clsx';
import { headerLinks } from '@/constant/config';
import { ChatHistory } from '@/chat/components/ChatHistory';
import { LanguageSelectionDropdown } from '@/components/common/LanguageSelectionDropdown';

export const MobileSidebar: React.FC = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const router = useRouter();

  const navigateTo = (path: string) => {
    router.push(path);
    setMenuOpen(false);
  };

  return (
    <div className='block md:hidden'>
      <div className='flex justify-between'>
        <div className='container p-4 flex items-center'>
          <button
            className='text-gray-800'
            onClick={() => setMenuOpen(!menuOpen)}
            aria-label='Toggle Menu'
          >
            {menuOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>
      </div>
      {/* Mobile Menu Panel */}
      <div
        className={clsx(
          'p-2 space-y-2 border border-gray-200 shadow-md bg-white h-full w-2/3 z-50 fixed top-0 left-0 transform transition-transform duration-300 ease-in-out',
          {
            'translate-x-0': menuOpen,
            '-translate-x-full': !menuOpen,
          },
        )}
      >
        <div className='flex items-center p-2'>
          <Image
            onClick={() => navigateTo('/')}
            src={'/images/logo.png'}
            alt={'logo'}
            width={30}
            height={30}
          />
          <span className='text-xl font-bold text-primary-600' onClick={() => navigateTo('/')}>
            grammr
          </span>
          <button
            className='absolute top-4 right-4 text-gray-800 hover:bg-gray-200 rounded-full'
            onClick={() => setMenuOpen(!menuOpen)}
            aria-label='Toggle Menu'
          >
            <X size={24} />
          </button>
        </div>
        {/* Nav Links */}
        {headerLinks.map((link) => (
          <p
            key={link.label}
            onClick={() => navigateTo(link.href)}
            className='flex items-center hover:bg-gray-200 rounded-full p-2'
          >
            <link.icon className='ml-2 mr-2' size={20} />
            {link.label}
          </p>
        ))}

        <div className='border border-gray-200 m-4 ' />

        <div className='flex items-center pl-4'>
          <SignedIn>
            <UserMenu click={() => setMenuOpen(false)} />
          </SignedIn>
          <SignedOut>
            <SignInButton>
              <button className='px-3 py-2 rounded-lg text-white text-sm bg-black'>Sign In</button>
            </SignInButton>
          </SignedOut>
          <ClerkLoading>
            <button className='px-3 py-2 rounded-lg text-white text-sm bg-black'>
              <LoadingSpinner size={4} textColor='white' spinnerColor='white' message='Loading' />
            </button>
          </ClerkLoading>
          <LanguageSelectionDropdown />
        </div>
        <div className='border border-gray-200 m-4 ' />
      </div>
      <SignedIn>
        <ChatHistory />
      </SignedIn>
      <SignedOut>
        <div className='p-4 text-center'>
          <p className='text-gray-500'>Please sign in to access chat history.</p>
          <SignInButton>
            <button className='mt-2 px-4 py-2 bg-blue-600 text-white rounded-lg'>Sign In</button>
          </SignInButton>
        </div>
      </SignedOut>
    </div>
  );
};
