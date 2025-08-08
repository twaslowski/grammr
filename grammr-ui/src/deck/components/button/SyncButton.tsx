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
  deleteNotes,
  updateNotes,
  precheckAnkiConnect,
} from '@/deck/components/button/anki-connect';
import { Flashcard } from '@/flashcard/types/flashcard';
import { fromFlashcard, Note } from '@/deck/types/note';

export default function SyncButton({ deck }: { deck: Deck; onSync: () => void }) {
  const { isLoading, request } = useApi();

  const triggerSync = async (deckId: string): Promise<Flashcard[]> => {
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
      toast({
        title: 'AnkiConnect Error',
        description:
          'AnkiConnect is not available. Please ensure Anki is running and AnkiConnect is installed.',
        variant: 'destructive',
      });
      return;
    }

    const flashcards = await triggerSync(deck.id);
    const toCreate: Note[] = flashcards
      .filter((f) => f.status === 'CREATED')
      .map((f) => fromFlashcard(f, deck.name));

    const toUpdate: Note[] = flashcards
      .filter((f) => f.status === 'UPDATED')
      .map((f) => fromFlashcard(f, deck.name));

    const toDelete: Note[] = flashcards
      .filter((f) => f.status === 'MARKED_FOR_DELETION')
      .map((f) => fromFlashcard(f, deck.name));

    try {
      await createDeck(deck.name);
      const created = await createNotes(toCreate);
      const updated = await updateNotes(toUpdate);
      const deleted = await deleteNotes(toDelete);

      const successfulSyncs = created.successfulSyncs
        .concat(updated.successfulSyncs)
        .concat(deleted.successfulSyncs);
      const failedSyncs = created.failedSyncs
        .concat(updated.failedSyncs)
        .concat(deleted.failedSyncs);

      void confirmSync(deck.id, successfulSyncs, failedSyncs);

      if (failedSyncs.length > 0) {
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
