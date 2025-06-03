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

export const MobileSidebar: React.FC = () => {
  const { languageSpoken, languageLearned, setLanguageSpoken, setLanguageLearned } = useLanguage();
  const [menuOpen, setMenuOpen] = useState(false);
  const router = useRouter();

  useEffect(() => {
    const savedSpoken = localStorage.getItem('languageSpoken');
    const savedLearned = localStorage.getItem('languageLearned');
    if (savedSpoken) setLanguageSpoken(savedSpoken);
    if (savedLearned) setLanguageLearned(savedLearned);
  }, [setLanguageSpoken, setLanguageLearned]);

  const updateLanguages = (spoken: string, learned: string) => {
    setLanguageSpoken(spoken);
    setLanguageLearned(learned);
    localStorage.setItem('languageSpoken', spoken);
    localStorage.setItem('languageLearned', learned);
  };

  const getLanguageByCode = (code: string) => {
    return languages.find((lang) => lang.code === code) || languages[0];
  };

  const navigateTo = (path: string) => {
    router.push(path);
    setMenuOpen(false);
  };

  const spokenLanguage = getLanguageByCode(languageSpoken);
  const learnedLanguage = getLanguageByCode(languageLearned);

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
          <DropdownMenu>
            <DropdownMenuTrigger className='m-4 inline-flex items-center px-3 py-2 border border-gray-300 rounded-lg text-sm font-medium text-gray-700 bg-white hover:bg-gray-50'>
              <span className='mr-1'>{spokenLanguage.flag}</span>
              <span className='mx-1'>→</span>
              <span className='mr-1'>{learnedLanguage.flag}</span>
              <ChevronDown className='ml-1 w-4 h-4' />
            </DropdownMenuTrigger>
            <DropdownMenuContent className='w-full p-4 mt-2'>
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
        </div>
        <div className='border border-gray-200 m-4 ' />
      </div>
    </div>
  );
};
