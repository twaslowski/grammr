import { SignOutButton, useUser } from '@clerk/nextjs';
import { Book, LogOut, User } from 'lucide-react';
import Link from 'next/link';
import React from 'react';

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';

const UserMenu: React.FC = () => {
  const { isSignedIn, user, isLoaded } = useUser();

  return (
    <DropdownMenu>
      <DropdownMenuTrigger className='inline-flex items-center px-3 py-2 border border-gray-300 rounded-md text-sm'>
        <User className='mr-2 h-4 w-4' />
        {user?.firstName || 'Profile'}
      </DropdownMenuTrigger>
      <DropdownMenuContent align='end'>
        <DropdownMenuItem asChild>
          <Link
            href='#'
            className='flex items-center text-gray-400 cursor-not-allowed'
            title='Profile not yet implemented'
            onClick={(e) => e.preventDefault()}
          >
            <User className='mr-2 h-4 w-4' />
            <span>Profile</span>
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem asChild>
          <Link href='/user/decks' className='flex items-center'>
            <Book className='mr-2 h-4 w-4' />
            <span>My Flashcards</span>
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem asChild>
          <Link
            href='#'
            className='flex items-center text-gray-400 cursor-not-allowed'
            title='Settings not yet implemented'
            onClick={(e) => e.preventDefault()}
          >
            <User className='mr-2 h-4 w-4' />
            <span>Settings</span>
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem>
          <div className='flex items-center space-x-2'>
            <LogOut className='mr-2 h-4 w-4' />
            <SignOutButton />
          </div>
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default UserMenu;
