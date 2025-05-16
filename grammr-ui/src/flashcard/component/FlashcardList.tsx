import { ChevronDown, ChevronRight, Edit, Trash2 } from 'lucide-react';
import { useRouter } from 'next/navigation';
import React, { MouseEvent, useState } from 'react';

import { Flashcard } from '@/flashcard/types/flashcard';

interface FlashcardListProps {
  cards: Flashcard[];
  deckId: number;
}

export default function FlashcardList(props: FlashcardListProps) {
  const [expandedCardId, setExpandedCardId] = useState<number | null>(null);
  const [visibleSide, setVisibleSide] = useState('front');
  const router = useRouter();

  const toggleCardExpansion = (cardId: number) => {
    if (expandedCardId === cardId) {
      setExpandedCardId(null);
    } else {
      setExpandedCardId(cardId);
      setVisibleSide('front');
    }
  };

  const toggleCardSide = () => {
    setVisibleSide(visibleSide === 'front' ? 'back' : 'front');
  };

  const handleDeleteCard = async (cardId: number, e: MouseEvent) => {
    e.stopPropagation();

    if (!confirm('Are you sure you want to delete this flashcard?')) {
      return;
    }

    try {
      const response = await fetch(`/api/v1/anki/flashcard/${cardId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error('Failed to delete card');
      }

      // Refresh the page to show updated card list
      window.location.reload();
    } catch (error) {
      console.error('Error deleting card:', error);
      alert('Failed to delete card. Please try again.');
    }
  };

  const handleEditCard = (cardId: number, e: MouseEvent) => {
    e.stopPropagation();
    router.push(`/user/decks/${props.deckId}/cards/${cardId}/edit`);
  };

  return (
    <div className='divide-y'>
      {props.cards.map((card) => (
        <div key={card.id} className='py-4'>
          <div
            className='flex items-center justify-between cursor-pointer'
            onClick={() => toggleCardExpansion(card.id)}
          >
            <div className='flex items-center'>
              {expandedCardId === card.id ? (
                <ChevronDown size={20} className='text-gray-500 mr-2' />
              ) : (
                <ChevronRight size={20} className='text-gray-500 mr-2' />
              )}
              <span className='line-clamp-1 font-medium'>
                {card.question.substring(0, 100)}
                {card.answer.length > 100 ? '...' : ''}
              </span>
            </div>

            <div className='flex space-x-2'>
              <button
                disabled
                onClick={(e) => handleEditCard(card.id, e)}
                className='p-1 text-gray-400 cursor-not-allowed'
                title='Edit Card'
              >
                <Edit size={16} />
              </button>

              <button
                onClick={(e) => handleDeleteCard(card.id, e)}
                className='p-1 text-red-400 hover:text-red-600'
                title='Delete Card'
              >
                <Trash2 size={16} />
              </button>
            </div>
          </div>

          {expandedCardId === card.id && (
            <div className='mt-4 ml-7'>
              <div className='flex justify-between items-center mb-2'>
                <div className='text-sm font-medium text-gray-500'>
                  {visibleSide === 'front' ? 'Front Side' : 'Back Side'}
                </div>
                <button
                  onClick={toggleCardSide}
                  className='text-xs px-2 py-1 bg-gray-100 rounded hover:bg-gray-200'
                >
                  Flip Card
                </button>
              </div>

              <div className='bg-gray-50 p-4 rounded whitespace-pre-wrap'>
                {visibleSide === 'front' ? card.question : card.answer}
              </div>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}
