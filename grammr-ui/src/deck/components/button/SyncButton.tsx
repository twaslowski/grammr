'use client';

import { Button } from '@/components/ui/button';
import React from 'react';
import Deck from '@/deck/types/deck';
import SyncIcon from '@/components/common/SyncIcon';
import { toast } from '@/hooks/use-toast';
import { useApi } from '@/hooks/useApi';

interface Note {
  fields: Fields;
  modelName: string;
  deckName: string;
}

interface Fields {
  front: string;
  back: string;
}

export default function SyncButton({ deck }: { deck: Deck }) {
  const { isLoading, error, request } = useApi();

  const fetchNonSyncedFlashcards = async (deckId: number): Promise<Deck> => {
    return await request<Deck>(`/api/v1/deck/sync`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        deckId: deckId,
      }),
    });
  };

  async function createDeck(deck: Deck) {
    await fetch('http://localhost:8765', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        action: 'createDeck',
        version: 6,
        params: {
          deck: deck.name,
        },
      }),
    });
  }

  async function createNotes(notes: Note[]) {
    return await fetch('http://localhost:8765', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        action: 'addNotes',
        version: 6,
        params: {
          notes: notes,
        },
      }),
    });
  }

  const syncFlashcards = async (deck: Deck) => {
    const nonSyncedFlashcards = await fetchNonSyncedFlashcards(deck.id);

    const notes: Note[] = nonSyncedFlashcards.flashcards.map((flashcard) => ({
      fields: {
        front: flashcard.question,
        back: flashcard.answer,
      },
      deckName: deck.name,
      modelName: 'Basic',
    }));

    try {
      await createDeck(deck);
      const result = await createNotes(notes);
      const data = await result.json();

      if (data.error) {
        toast({
          title: 'Error',
          description: `Failed to sync flashcards: ${data.error}`,
          variant: 'destructive',
        });
      } else {
        toast({
          title: 'Success',
          description: 'Flashcards synced successfully!',
          variant: 'default',
        });
      }
    } catch {
      toast({
        title: 'Error',
        description:
          'Failed to sync flashcards. Ensure that Anki is running with the AnkiConnect plugin installed.',
        variant: 'destructive',
      });
    }
  };

  return (
    <Button
      onClick={() => syncFlashcards(deck)}
      disabled={isLoading || !deck?.id}
      className='flex items-center px-3 py-2 rounded hover:bg-blue-50 bg-blue-100 text-blue-800'
      variant='outline'
    >
      <span className={isLoading ? 'animate-spin' : ''}>
        <SyncIcon className='text-blue-800' />
      </span>
      {isLoading ? 'Syncing ...' : 'Sync'}
    </Button>
  );
}
