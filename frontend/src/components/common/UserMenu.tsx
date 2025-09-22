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
import Image from 'next/image';

const UserMenu: React.FC<{ click?: () => void }> = ({ click = () => {} }) => {
  const { user } = useUser();

  return (
    <DropdownMenu>
      <DropdownMenuTrigger>
        {user?.hasImage ? (
          <Image
            src={user.imageUrl}
            alt={`${user.firstName} ${user.lastName}`}
            className='mr-2 h-8 w-8 rounded-full border-gray-500 border'
            width={8}
            height={8}
          />
        ) : (
          <div className='inline-flex items-center px-3 py-2 bg-gray-950 text-white rounded-md text-sm'>
            <User className='mr-2 h-4 w-4' />
            {user?.firstName || 'User'}
          </div>
        )}
      </DropdownMenuTrigger>
      <DropdownMenuContent align='end'>
        <DropdownMenuItem asChild onClick={click}>
          <Link href='/user/profile' className='flex items-center'>
            <User className='mr-2 h-4 w-4' />
            <span>Profile</span>
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem asChild onClick={click}>
          <Link href='/user/decks' className='flex items-center'>
            <Book className='mr-2 h-4 w-4' />
            <span>My Flashcards</span>
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem asChild onClick={click}>
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
        <DropdownMenuItem onClick={click}>
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
