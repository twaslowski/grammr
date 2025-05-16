'use client';

import { useRouter } from 'next/navigation';
import React, { useEffect, useState } from 'react';

import LoadingSpinner from '@/components/common/LoadingSpinner';
import Unauthorized from '@/components/common/Unauthorized';
import DeckCard from '@/flashcard/component/deck/DeckCard';
import EmptyState from '@/flashcard/component/deck/EmptyDeckState';
import NewDeckDialog from '@/flashcard/component/deck/NewDeckDialog';
import { toast } from '@/hooks/use-toast';
import Deck from '@/flashcard/types/deck';
import Error from '@/components/common/Error';

export default function DecksPage() {
  const [decks, setDecks] = useState<Deck[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [unauthorized, setUnauthorized] = useState(false);
  const [showNewDeckDialog, setShowNewDeckDialog] = useState(false);
  const router = useRouter();

  const createNewDeck = async (name: string, description: string) => {
    try {
      setIsLoading(true);
      const response = await fetch('/api/v1/anki/deck', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, description }),
        credentials: 'same-origin',
      });
      const newDeck = await response.json();
      setDecks([...decks, newDeck]);
      setShowNewDeckDialog(false);
    } catch (error) {
      toast({
        title: 'Error creating deck',
        description: 'Please try again later',
        variant: 'destructive',
      });
    } finally {
      setIsLoading(false);
    }
  };

  const fetchDecks = async (): Promise<Deck[]> => {
    const response = await fetch('/api/v1/anki/deck');

    if (response.status === 401) {
      setUnauthorized(true);
      return [];
    }

    if (!response.ok) {
      setError('Failed to fetch decks');
      return [];
    }

    return (await response.json()) as Deck[];
  };

  useEffect(() => {
    fetchDecks()
      .then((r) => setDecks(r))
      .catch((err) => setError(err.message))
      .finally(() => setIsLoading(false));
  }, []);

  const handleDeckClick = (deckId: number) => {
    router.push(`/user/decks/${deckId}`);
  };

  if (isLoading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return (
      <Error
        title={'Failed to load decks :('}
        message={'An unexpected error occurred when loading your decks. Please try again later.'}
      />
    );
  }

  if (unauthorized) {
    return (
      <div className='flex flex-col items-center justify-center min-h-screen p-4'>
        <Unauthorized />
      </div>
    );
  }

  if (decks.length === 0) {
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
          onCreate={createNewDeck}
          isLoading={isLoading}
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
            <DeckCard key={deck.id} deck={deck} onClick={() => handleDeckClick(deck.id)} />
          ))}
        </div>

        <NewDeckDialog
          isOpen={showNewDeckDialog}
          onClose={() => setShowNewDeckDialog(false)}
          onCreate={createNewDeck}
          isLoading={isLoading}
        />
      </div>
    </main>
  );
}
