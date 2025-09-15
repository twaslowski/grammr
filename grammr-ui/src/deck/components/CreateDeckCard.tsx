'use client';

import React from 'react';
import { Plus } from 'lucide-react';

interface CreateDeckCardProps {
  onClickAction: () => void;
}

export default function CreateDeckCard({ onClickAction }: CreateDeckCardProps) {
  return (
    <div
      onClick={onClickAction}
      className='rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow duration-200 cursor-pointer flex flex-col h-full border-dashed border-2 border-gray-300 hover:border-primary-400'
    >
      <div className='flex-1 p-6 flex items-center justify-center bg-gray-50 hover:bg-primary-50 transition-colors duration-200'>
        <div className='text-center'>
          <Plus className='w-8 h-8 mx-auto mb-3 text-gray-400' />
          <div className='font-medium text-gray-600 mb-1'>Create New Deck</div>
          <div className='text-sm text-gray-500'>Start building your flashcard collection</div>
        </div>
      </div>

      <div className='border-t p-3 flex justify-center items-center bg-gray-50'>
        <span className='text-sm font-medium text-gray-600'>Click to Create</span>
      </div>
    </div>
  );
}
