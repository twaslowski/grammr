'use client';

import { ArrowLeft, Trash2 } from 'lucide-react';
import { useRouter } from 'next/navigation';
import React, { use, useCallback, useEffect, useState } from 'react';

import ExportButton from '@/deck/components/button/ExportButton';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import NotFound from '@/components/common/NotFound';
import FlashcardList from '@/flashcard/components/FlashcardList';
import Deck from '@/deck/types/deck';
import SyncButton from '@/deck/components/button/SyncButton';
import { Button } from '@/components/ui/button';
import { toast } from '@/hooks/use-toast';
import { useApi } from '@/hooks/useApi';
import { Flashcard } from '@/flashcard/types/flashcard';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import GenericFlashcardPreview from '@/flashcard/components/GenericFlashcardPreview';

export default function DeckPage(props: { params: Promise<{ deckId: string }> }) {
  const { deckId } = use(props.params);
  const { isLoading, error, request } = useApi();
  const [deck, setDeck] = useState<Deck | null>(null);
  const [flashcards, setFlashcards] = useState<Flashcard[]>([]);
  const [showPreviewDialog, setShowPreviewDialog] = useState(false);
  const router = useRouter();

  const handleDeleteDeck = async () => {
    if (!confirm('Are you sure you want to delete this deck? This action cannot be undone.')) {
      return;
    }

    await fetch(`/api/v2/deck/${deckId}`, {
      method: 'DELETE',
    })
      .then(() => {
        router.push('/user/decks');
      })
      .catch(() => {
        toast({
          title: 'Error',
          description: 'Failed to delete deck. Please try again later.',
          variant: 'destructive',
        });
      });
  };

  // Move fetchFlashcards outside useEffect so it can be called from anywhere
  const fetchFlashcards = useCallback(async () => {
    const flashcards = await request<Flashcard[]>(`/api/v2/deck/${deckId}/flashcard`, {
      method: 'GET',
    });
    setFlashcards(flashcards);
  }, [deckId, request]);

  useEffect(() => {
    const fetchDeck = async () => {
      const fetchedDeck = await request<Deck>(`/api/v2/deck/${deckId}`, {
        method: 'GET',
      });
      setDeck(fetchedDeck);
    };

    void fetchDeck();
    void fetchFlashcards();
  }, [deckId, request, fetchFlashcards]);

  if (isLoading) {
    return <LoadingSpinner message='Loading deck...' />;
  }

  if (error && error.code === 404) {
    return <NotFound message='Deck not found' backHref='/user/decks' />;
  }

  if (error) {
    return (
      <div className='flex flex-col items-center justify-center min-h-screen p-4'>
        <div className='text-red-500 mb-4'>Error: {error.message}</div>
        <button
          onClick={() => window.location.reload()}
          className='px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600'
        >
          Try Again
        </button>
      </div>
    );
  }

  if (deck)
    return (
      <div className='container mx-auto px-4 py-8'>
        <button
          onClick={() => router.push('/user/decks')}
          className='flex items-center text-blue-600 hover:text-blue-800 mb-6'
        >
          <ArrowLeft size={16} className='mr-1' />
          Back to Decks
        </button>

        <div className='bg-white rounded-lg shadow-md p-6 mb-8'>
          <div className='flex flex-col md:flex-row md:justify-between md:items-center mb-4'>
            <div>
              <h1 className='text-2xl font-bold mb-2'>{deck.name}</h1>
              <p className='text-gray-600 mb-4'>{deck.description}</p>
            </div>

            <div className='flex space-x-2 mt-4 md:mt-0'>
              <ExportButton deck={deck} />
              <SyncButton deck={deck} onSync={fetchFlashcards} />

              <Button
                onClick={handleDeleteDeck}
                className='flex items-center px-3 py-2 rounded hover:bg-red-50 bg-red-100 text-red-800'
                variant='outline'
              >
                <Trash2 size={16} className='mr-1' />
                <span className='hidden sm:inline'>Delete</span>
              </Button>
            </div>
          </div>

          <div className='grid grid-cols-1 md:grid-cols-3 gap-4 text-sm'>
            <div className='bg-gray-50 p-3 rounded'>
              <div className='text-gray-500 mb-1'>Total Cards</div>
              <div className='font-semibold'>{flashcards.length || 0}</div>
            </div>

            <div className='bg-gray-50 p-3 rounded'>
              <div className='text-gray-500 mb-1'>Created On</div>
              <div className='font-semibold'>
                {new Date(deck.createdTimestamp).toLocaleDateString('en-US', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                })}
              </div>
            </div>

            <div className='bg-gray-50 p-3 rounded'>
              <div className='text-gray-500 mb-1'>Last Modified</div>
              <div className='font-semibold'>
                {new Date(deck.updatedTimestamp).toLocaleDateString('en-US', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                })}
              </div>
            </div>
          </div>
        </div>

        <div className='bg-white rounded-lg shadow-md p-6'>
          <div className='flex justify-between items-center mb-6'>
            <h2 className='text-xl font-semibold'>Flashcards</h2>
            <Button onClick={() => setShowPreviewDialog(true)}>Add card</Button>
            <Dialog open={showPreviewDialog} onOpenChange={setShowPreviewDialog}>
              <DialogContent className='max-w-3xl'>
                <DialogHeader>
                  <DialogTitle>Preview Flashcard</DialogTitle>
                </DialogHeader>
                <GenericFlashcardPreview
                  initialDeckId={deck.id}
                  initialFront={''}
                  initialBack={''}
                  onClose={() => setShowPreviewDialog(false)}
                  onCardAdded={fetchFlashcards}
                />
              </DialogContent>
            </Dialog>
          </div>

          {flashcards.length > 0 ? (
            <FlashcardList cards={flashcards} deckId={deck.id} />
          ) : (
            <div className='text-center py-12 bg-gray-50 rounded-lg'>
              <p className='text-gray-500 mb-4'>This deck doesn't have any flashcards yet.</p>
            </div>
          )}
        </div>
      </div>
    );
}
