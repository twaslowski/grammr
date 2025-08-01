import { PlusCircle } from 'lucide-react';
import React from 'react';

import { Button } from '@/components/ui/button';

interface EmptyStateProps {
  title: string;
  description: string;
  actionText: string;
  onClick: () => void;
}

export default function EmptyState(emptyStateProps: EmptyStateProps) {
  return (
    <div className='flex flex-col items-center justify-center min-h-screen text-center p-4'>
      <div className='bg-gray-100 rounded-full p-6 mb-4'>
        <PlusCircle size={48} className='text-gray-400' />
      </div>
      <h2 className='text-xl font-semibold mb-2'>{emptyStateProps.title}</h2>
      <p className='text-gray-600 mb-6 max-w-md'>{emptyStateProps.description}</p>
      <Button
        onClick={emptyStateProps.onClick}
        className='px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors'
      >
        {emptyStateProps.actionText}
      </Button>
    </div>
  );
}
