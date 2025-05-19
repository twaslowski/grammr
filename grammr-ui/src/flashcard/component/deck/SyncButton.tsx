'use client';

import { Button } from '@/components/ui/button';
import React from 'react';
import Deck from '@/flashcard/types/deck';
import SyncIcon from '@/components/common/SyncIcon';

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
  const [isExporting, setIsExporting] = React.useState(false);

  const syncFlashcards = async (deck: Deck) => {
    setIsExporting(true);
    const notes: Note[] = deck.flashcards.map((flashcard) => ({
      fields: {
        front: flashcard.question,
        back: flashcard.answer,
      },
      deckName: deck.name,
      modelName: 'Basic',
    }));

    try {
      const deckResult = await fetch('http://localhost:8765', {
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

      const deckData = await deckResult.json();
      if (deckData.error) {
        console.error('Error creating deck:', deckData.error);
        alert('Failed to create deck. Please try again.');
        return;
      }

      const result = await fetch('http://localhost:8765', {
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
      const data = await result.json();
      if (data.error) {
        console.error('Error syncing flashcards:', data.error);
        alert('Failed to sync flashcards. Please try again.');
      } else {
        alert('Flashcards synced successfully!');
      }
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <Button
      onClick={() => syncFlashcards(deck)}
      disabled={isExporting || !deck?.id}
      className='flex items-center px-3 py-2 rounded bg-blue-100 text-blue-800'
      variant='outline'
    >
      <span className={isExporting ? 'animate-spin' : ''}>
        <SyncIcon className='text-blue-800' />
      </span>
      {isExporting ? 'Syncing ...' : 'Sync'}
    </Button>
  );
}
