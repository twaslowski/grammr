'use client';

import React, { useEffect } from 'react';
import { useLanguage } from '@/context/LanguageContext';
import { languages } from '@/constant/languages';
import Link from 'next/link';
import { ChevronDown } from 'lucide-react';
import { ClerkLoading, SignedIn, SignedOut, SignInButton } from '@clerk/nextjs';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import UserMenu from '@/components/common/UserMenu';
import Image from 'next/image';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { headerLinks } from '@/constant/config';

export const DesktopHeader: React.FC = () => {
  const { languageSpoken, languageLearned, setLanguageSpoken, setLanguageLearned } = useLanguage();

  useEffect(() => {
    const savedSpoken = localStorage.getItem('languageSpoken');
    const savedLearned = localStorage.getItem('languageLearned');

    if (savedSpoken) setLanguageSpoken(savedSpoken);
    if (savedLearned) setLanguageLearned(savedLearned);
  }, [setLanguageSpoken, setLanguageLearned]);

  // Update localStorage and notify parent when language changes
  const updateLanguages = (spoken: string, learned: string) => {
    setLanguageSpoken(spoken);
    setLanguageLearned(learned);

    localStorage.setItem('languageSpoken', spoken);
    localStorage.setItem('languageLearned', learned);
  };

  // Helper function to get language details by code
  const getLanguageByCode = (code: string) => {
    return languages.find((lang) => lang.code === code) || languages[0];
  };

  // Get current language objects
  const spokenLanguage = getLanguageByCode(languageSpoken);
  const learnedLanguage = getLanguageByCode(languageLearned);

  return (
    <header className='bg-white shadow-sm h-min py-2 hidden md:block'>
      <div className='container mx-auto px-4'>
        <div className='flex items-center justify-between'>
          {/* Logo and Brand */}
          <div className='flex items-center'>
            <Link href='/' className='flex items-start gap-2'>
              <Image src={'/images/logo.png'} alt={'logo'} width={30} height={30} />
              <span className='text-xl font-bold text-primary-600'>grammr</span>
            </Link>
          </div>

          {/* Main navigation links */}
          <nav className='space-x-16'>
            {headerLinks.map((link) => (
              <Link key={link.label} href={link.href} className='custom-link'>
                {link.label}
              </Link>
            ))}
          </nav>

          <div className='flex items-center space-x-4 -mr-4'>
            <DropdownMenu>
              <DropdownMenuTrigger className='inline-flex items-center px-3 py-2 border border-gray-300 rounded-lg text-sm font-medium text-gray-700 bg-white hover:bg-gray-50'>
                <span className='mr-1'>{spokenLanguage.flag}</span>
                <span className='mx-1'>→</span>
                <span className='mr-1'>{learnedLanguage.flag}</span>
                <ChevronDown className='ml-1 w-4 h-4' />
              </DropdownMenuTrigger>
              <DropdownMenuContent className='w-64 p-4'>
                <div className='mb-4'>
                  <p className='text-sm font-medium text-gray-700 mb-2'>I speak:</p>
                  <div className='grid grid-cols-2 gap-2'>
                    {languages.map((lang) => (
                      <button
                        key={`speak-${lang.code}`}
                        className={`flex items-center px-2 py-2 text-sm rounded-lg ${
                          languageSpoken === lang.code
                            ? 'bg-blue-100 text-blue-700'
                            : 'hover:bg-gray-100'
                        }`}
                        onClick={() => updateLanguages(lang.code, languageLearned)}
                      >
                        <span className='mr-2'>{lang.flag}</span>
                        <span>{lang.name}</span>
                      </button>
                    ))}
                  </div>
                </div>
                <div>
                  <p className='text-sm font-medium text-gray-700 mb-2'>I am learning:</p>
                  <div className='grid grid-cols-2 gap-2'>
                    {languages.map((lang) => (
                      <button
                        key={`learn-${lang.code}`}
                        className={`flex items-center px-2 py-2 text-sm rounded-lg ${
                          languageLearned === lang.code
                            ? 'bg-blue-100 text-blue-700'
                            : 'hover:bg-gray-100'
                        }`}
                        onClick={() => updateLanguages(languageSpoken, lang.code)}
                      >
                        <span className='mr-2'>{lang.flag}</span>
                        <span>{lang.name}</span>
                      </button>
                    ))}
                  </div>
                </div>
              </DropdownMenuContent>
            </DropdownMenu>
            <SignedIn>
              <UserMenu />
            </SignedIn>
            <SignedOut>
              <div className='flex items-center'>
                <SignInButton>
                  <button className='px-3 py-2 rounded-lg text-white text-sm bg-black'>
                    Sign In
                  </button>
                </SignInButton>
              </div>
            </SignedOut>
            <ClerkLoading>
              <button className='px-3 py-2 rounded-lg text-white text-sm bg-black'>
                <LoadingSpinner size={4} textColor='white' spinnerColor='white' message='Loading' />
              </button>
            </ClerkLoading>
          </div>
        </div>
      </div>
    </header>
  );
};
