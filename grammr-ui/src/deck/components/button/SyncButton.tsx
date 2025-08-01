'use client';

import { Button } from '@/components/ui/button';
import React from 'react';
import Deck from '@/deck/types/deck';
import SyncIcon from '@/components/common/SyncIcon';
import { toast } from '@/hooks/use-toast';
import { useApi } from '@/hooks/useApi';
import { Sync } from '@/flashcard/types/sync';

interface Note {
  fields: Fields;
  modelName: string;
  deckName: string;
}

interface Fields {
  front: string;
  back: string;
}

export default function SyncButton({ deck }: { deck: Deck; onSync: () => void }) {
  const { isLoading, error, request } = useApi();

  const precheckAnkiConnect = async () => {
    try {
      const response = await fetch('http://localhost:8765', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          action: 'version',
          version: 6,
        }),
      });

      if (!response.ok) {
        throw new Error('AnkiConnect is not running or not reachable.');
      }

      const data = await response.json();
      if (data.error) {
        throw new Error(data.error);
      }
    } catch (err) {
      toast({
        title: 'Error',
        description:
          'AnkiConnect is not running or not reachable. Please ensure Anki is open with the AnkiConnect plugin installed.',
        variant: 'destructive',
      });
      throw err;
    }
  };

  const performSync = async (deckId: string): Promise<Sync> => {
    return await request<Sync>(`/api/v2/deck/${deckId}/sync`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    });
  };

  const confirmSync = async (deckId: string, syncId: string): Promise<void> => {
    return await request<void>(`/api/v2/deck/${deckId}/sync/${syncId}/confirm`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
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
    // Check if AnkiConnect is available
    try {
      await precheckAnkiConnect();
    } catch (err) {
      return;
    }

    const sync = await performSync(deck.id);
    const notes: Note[] = sync.flashcards.map((flashcard) => ({
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
        void confirmSync(deck.id, sync.syncId);
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
