'use client';

import React from 'react';

import Deck from '@/deck/types/deck';
import SyncButton from '@/deck/components/button/SyncButton';
import { useRouter } from 'next/navigation';

interface DeckCardProps {
  deck: Deck;
}

export default function DeckCard(props: DeckCardProps) {
  const deck = props.deck;
  const router = useRouter();

  // const cardCount = deck.flashcards.length;
  const lastUpdated = new Date(deck.updatedTimestamp).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  });

  return (
    <div className='border rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow duration-200 cursor-pointer flex flex-col h-full'>
      <div onClick={() => router.push(`/user/decks/${deck.id}`)}>
        <div className='h-24 p-6 flex items-center justify-center text-white font-bold text-xl bg-primary-500'>
          {deck.name}
        </div>

        <div className='p-4 flex-1'>
          <div className='mb-2 line-clamp-2 h-12'>{deck.description || 'No description'}</div>

          {/*<div className='text-sm text-gray-600 mt-4'>*/}
          {/*  {cardCount} {cardCount === 1 ? 'card' : 'cards'}*/}
          {/*</div>*/}

          <div className='text-xs text-gray-500 mt-1'>Last updated: {lastUpdated}</div>
        </div>
      </div>
      <div className='border-t p-3 flex justify-between items-center'>
        <span className='text-sm font-medium'>View Details</span>
        <SyncButton deck={deck} onSync={() => {}} />
      </div>
    </div>
  );
}
