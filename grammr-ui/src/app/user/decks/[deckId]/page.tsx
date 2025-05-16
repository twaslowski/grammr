'use client';

import { ArrowLeft, Edit, Plus, Trash2 } from 'lucide-react';
import { useRouter } from 'next/navigation';
import React, { use, useEffect, useState } from 'react';

import DisabledButton from '@/components/buttons/DisabledButton';
import ExportButton from '@/components/buttons/ExportButton';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import NotFound from '@/components/common/NotFound';
import FlashcardList from '@/flashcard/component/FlashcardList';
import Deck from '@/flashcard/types/deck';

export default function DeckPage(props: { params: Promise<{ deckId: string }> }) {
  const params = use(props.params);
  const { deckId } = params;
  const [deck, setDeck] = useState<Deck | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [notFound, setNotFound] = useState(false);
  const router = useRouter();

  const handleDeleteDeck = async () => {
    if (!confirm('Are you sure you want to delete this deck? This action cannot be undone.')) {
      return;
    }

    try {
      const response = await fetch(`/api/v1/anki/deck/${deckId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error('Failed to delete deck');
      }

      router.push('/user/decks');
    } catch (error) {
      console.error('Error deleting deck:', error);
      alert('Failed to delete deck. Please try again.');
    }
  };

  useEffect(() => {
    async function fetchDeck() {
      try {
        setIsLoading(true);
        const response = await fetch(`/api/v1/anki/deck/${deckId}`);

        if (response.status === 404) {
          setNotFound(true);
          return;
        }

        if (!response.ok) {
          throw new Error('Failed to fetch deck');
        }

        const data = await response.json();
        setDeck(data);
      } catch (error) {
        if (error instanceof Error) setError(error.message);
      } finally {
        setIsLoading(false);
      }
    }

    fetchDeck();
  }, [deckId]);

  if (isLoading) {
    return <LoadingSpinner message='Loading deck...' />;
  }

  if (notFound) {
    return <NotFound message='Deck not found' backHref='/user/decks' />;
  }

  if (error) {
    return (
      <div className='flex flex-col items-center justify-center min-h-screen p-4'>
        <div className='text-red-500 mb-4'>Error: {error}</div>
        <button
          onClick={() => window.location.reload()}
          className='px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600'
        >
          Try Again
        </button>
      </div>
    );
  }

  if (!deck) {
    return null;
  }

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
            <DisabledButton
              tooltipText='Editing decks is not supported yet.'
              className='flex items-center px-3 py-2 rounded'
              icon={<Edit size={16} />}
            >
              Edit
            </DisabledButton>

            <ExportButton deck={deck} />

            <button
              onClick={handleDeleteDeck}
              className='flex items-center px-3 py-2 bg-red-100 text-red-600 rounded'
              title='Delete Deck'
            >
              <Trash2 size={16} className='mr-1' />
              <span className='hidden sm:inline'>Delete</span>
            </button>
          </div>
        </div>

        <div className='grid grid-cols-1 md:grid-cols-3 gap-4 text-sm'>
          <div className='bg-gray-50 p-3 rounded'>
            <div className='text-gray-500 mb-1'>Total Cards</div>
            <div className='font-semibold'>{deck.flashcards.length || 0}</div>
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
          <DisabledButton
            tooltipText='Adding cards is currently possible from text translation only.'
            className='flex items-center px-3 py-2 rounded'
            icon={<Plus size={16} />}
          >
            Add card
          </DisabledButton>
        </div>

        {deck.flashcards && deck.flashcards.length > 0 ? (
          <FlashcardList cards={deck.flashcards} deckId={deck.id} />
        ) : (
          <div className='text-center py-12 bg-gray-50 rounded-lg'>
            <p className='text-gray-500 mb-4'>This deck doesn't have any flashcards yet.</p>
          </div>
        )}
      </div>
    </div>
  );
}
