'use client';

import { Button } from '@/components/ui/button';
import React from 'react';
import Deck from '@/deck/types/deck';
import SyncIcon from '@/components/common/SyncIcon';
import { toast } from '@/hooks/use-toast';
import { useApi } from '@/hooks/useApi';
import {
  createDeck,
  createNotes,
  getNote,
  precheckAnkiConnect,
} from '@/deck/components/button/anki-connect';
import { Flashcard } from '@/flashcard/types/flashcard';

export default function SyncButton({ deck }: { deck: Deck; onSync: () => void }) {
  const { isLoading, request } = useApi();

  const performSync = async (deckId: string): Promise<Flashcard[]> => {
    return await request<Flashcard[]>(`/api/v2/deck/${deckId}/sync`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    });
  };

  const confirmSync = async (
    deckId: string,
    successfulSyncs: string[],
    failedSyncs: string[],
  ): Promise<void> => {
    return await request<void>(`/api/v2/deck/${deckId}/sync/confirm`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        successfulSyncs,
        failedSyncs,
      }),
      credentials: 'include',
    });
  };

  const syncFlashcards = async (deck: Deck) => {
    // Check if AnkiConnect is available
    try {
      await precheckAnkiConnect();
    } catch (err) {
      return;
    }

    const flashcards = await performSync(deck.id);
    const notes: Note[] = flashcards.map((flashcard) => ({
      fields: {
        front: flashcard.question,
        back: flashcard.answer,
      },
      deckName: deck.name,
      modelName: 'Basic',
      id: flashcard.id,
    }));

    try {
      await createDeck(deck);
      const result = await createNotes(notes);
      void confirmSync(deck.id, result.successfulSyncs, result.failedSyncs);

      if (result.failedSyncs && result.failedSyncs.length > 0) {
        toast({
          title: 'Error',
          description: `Failed to sync some flashcards`,
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
        description: 'An unexpected error occurred while syncing flashcards to Anki.',
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
