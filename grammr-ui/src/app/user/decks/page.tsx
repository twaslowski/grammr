'use client';

import React, { useState } from 'react';

import LoadingSpinner from '@/components/common/LoadingSpinner';
import Unauthorized from '@/components/common/Unauthorized';
import DeckCard from '@/deck/components/DeckCard';
import NewDeckDialog from '@/deck/components/NewDeckDialog';
import Error from '@/components/common/Error';
import { useDecks } from '@/deck/hooks/useDecks';
import CreateDeckCard from '@/deck/components/CreateDeckCard';

export default function DecksPage() {
  const { decks, addDeck, isLoading, error, refreshDecks } = useDecks();
  const [showNewDeckDialog, setShowNewDeckDialog] = useState(false);

  if (isLoading) {
    return (
      <div className='flex flex-col items-center justify-center min-h-screen p-4'>
        <LoadingSpinner size={98} />
      </div>
    );
  }

  if (error && error.code === 401) {
    return (
      <div className='flex flex-col items-center justify-center min-h-screen p-4'>
        <Unauthorized />
      </div>
    );
  }

  if (error) {
    return (
      <div className='flex flex-col items-center justify-center min-h-screen p-4'>
        <Error title='Failed to load decks'>
          An unexpected error occurred when loading your decks. Please try again later.
        </Error>
      </div>
    );
  }

  return (
    <main>
      <div className='container mx-auto px-4 py-12 flex-grow'>
        <div className='flex justify-between items-center mb-8'>
          <h1 className='text-2xl font-bold'>Your Decks</h1>
        </div>

        <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6'>
          {decks.map((deck) => (
            <DeckCard key={deck.id} deck={deck} />
          ))}
          <CreateDeckCard onClickAction={() => setShowNewDeckDialog(true)} />
        </div>

        <NewDeckDialog
          isOpen={showNewDeckDialog}
          onClose={() => setShowNewDeckDialog(false)}
          onCreate={addDeck}
          onImport={refreshDecks}
        />
      </div>
    </main>
  );
}
