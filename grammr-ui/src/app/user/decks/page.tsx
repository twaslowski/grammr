'use client';

import React, { useState } from 'react';

import LoadingSpinner from '@/components/common/LoadingSpinner';
import Unauthorized from '@/components/common/Unauthorized';
import DeckCard from '@/deck/components/DeckCard';
import EmptyState from '@/deck/components/EmptyDeckState';
import NewDeckDialog from '@/deck/components/NewDeckDialog';
import Error from '@/components/common/Error';
import { useDecks } from '@/deck/hooks/useDecks';

export default function DecksPage() {
  const { decks, addDeck, isLoading, error } = useDecks();
  const [showNewDeckDialog, setShowNewDeckDialog] = useState(false);

  if (isLoading) {
    return <LoadingSpinner size={98} />;
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
      <Error
        title={'Failed to load decks :('}
        message={'An unexpected error occurred when loading your decks. Please try again later.'}
      />
    );
  }

  if (!isLoading && decks.length === 0) {
    return (
      <div>
        <EmptyState
          title='No Decks Found'
          description='Create your first flashcard deck to get started.'
          actionText='Create Deck'
          onClick={() => setShowNewDeckDialog(true)}
        />
        <NewDeckDialog
          isOpen={showNewDeckDialog}
          onClose={() => setShowNewDeckDialog(false)}
          onCreate={addDeck}
        />
      </div>
    );
  }

  return (
    <main>
      <div className='container mx-auto px-4 py-12 flex-grow'>
        <div className='flex justify-between items-center mb-8'>
          <h1 className='text-2xl font-bold'>Your Decks</h1>
          <button
            onClick={() => setShowNewDeckDialog(true)}
            className='px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600'
          >
            Create New Deck
          </button>
        </div>

        <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6'>
          {decks.map((deck) => (
            <DeckCard key={deck.id} deck={deck} />
          ))}
        </div>

        <NewDeckDialog
          isOpen={showNewDeckDialog}
          onClose={() => setShowNewDeckDialog(false)}
          onCreate={addDeck}
        />
      </div>
    </main>
  );
}
