import { RotateCw } from 'lucide-react';
import React, { useEffect, useState } from 'react';

import DeckSelection from '@/deck/components/DeckSelection';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { Textarea } from '@/components/ui/textarea';
import { toast } from '@/hooks/use-toast';
import { Flashcard } from '@/flashcard/types/flashcard';
import { useApi } from '@/hooks/useApi';
import { Paradigm } from '@/flashcard/types/paradigm';
import RichFlashcardContent from '@/flashcard/components/RichFlashcardContent';
import { useInflections } from '@/inflection/hooks/use-inflections';

interface FlashcardPreviewProps {
  initialDeckId?: string;
  initialFront: string;
  initialBack: string;
  onClose: () => void;
  onCardAdded?: () => void;
  flashcardId?: string;
  paradigm: Paradigm | null;
  submitAction?: 'create' | 'update';
}

const GenericFlashcardPreview: React.FC<FlashcardPreviewProps> = ({
  initialDeckId = '-1',
  onCardAdded = () => {},
  initialFront,
  initialBack,
  onClose,
  paradigm,
  flashcardId = '',
  submitAction = 'create',
}) => {
  const { isLoading, request } = useApi();
  const [deckId, setDeckId] = useState<string>(initialDeckId);
  const [front, setFront] = useState(initialFront);
  const [back, setBack] = useState(initialBack);
  const [activeCard, setActiveCard] = useState('front');

  const handleToggle = () => {
    if (activeCard === 'front') {
      setActiveCard('back');
    } else {
      setActiveCard('front');
    }
  };

  const handleSubmit = () => {
    if (submitAction === 'create') {
      void createFlashcard();
    } else {
      void handleUpdateCard(flashcardId);
    }
  };

  const createFlashcard = async () => {
    await request<Flashcard>(`/api/v2/deck/${deckId}/flashcard`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        deckId: deckId,
        question: front,
        answer: back,
      }),
    })
      .then(() => {
        toast({
          title: 'Success',
          description: 'Flashcard created successfully',
        });
        onClose();
      })
      .catch(() => {
        toast({
          title: 'Error',
          description: 'Error creating flashcard',
          variant: 'destructive',
        });
      })
      .finally(() => {
        onCardAdded();
      });
  };

  const handleUpdateCard = async (cardId: string) => {
    await fetch(`/api/v2/deck/${deckId}/flashcard/${cardId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        deckId: deckId,
        question: front,
        answer: back,
      }),
    })
      .then(() => {
        toast({
          title: 'Success',
          description: 'Flashcard updated successfully',
        });
        onClose();
      })
      .catch(() => {
        toast({
          title: 'Error',
          description: 'Error updating flashcard',
          variant: 'destructive',
        });
      })
      .finally(() => onCardAdded());
  };

  return (
    <div>
      <div className='mb-8 flex rounded-t border-b overflow-hidden'>
        <button
          className={`py-2 px-6 font-medium transition-colors ${
            activeCard === 'front'
              ? 'bg-white text-blue-600 border-t border-l border-r border-gray-300 rounded-t-md -mb-px'
              : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
          }`}
          onClick={() => setActiveCard('front')}
        >
          Front
        </button>
        <button
          className={`py-2 px-6 font-medium transition-colors ${
            activeCard === 'back'
              ? 'bg-white text-blue-600 border-t border-l border-r border-gray-300 rounded-t-md -mb-px'
              : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
          }`}
          onClick={() => setActiveCard('back')}
        >
          Back
        </button>
      </div>
      <div>
        <Card className='w-full h-32 cursor-pointer relative' onClick={handleToggle}>
          <RotateCw className='h-4 w-4 absolute top-4 right-4' />
          <CardContent className='flex items-center justify-center h-full p-0'>
            <div className='text-xl'>
              {activeCard === 'front' ? (
                front
              ) : paradigm ? (
                <RichFlashcardContent front={front} back={back} paradigm={paradigm} />
              ) : (
                back
              )}
            </div>
          </CardContent>
        </Card>
      </div>

      <div className='space-y-4 mt-6'>
        <div className='flex gap-4'>
          <div className='w-1/2'>
            <h3 className='text-sm font-medium mb-2'>Front</h3>
            <Textarea
              value={front}
              onChange={(e) => setFront(e.target.value)}
              placeholder='Front of card'
              className='min-h-20'
            />
          </div>
          <div className='w-1/2'>
            <h3 className='text-sm font-medium mb-2'>Back</h3>
            <Textarea
              value={back}
              onChange={(e) => setBack(e.target.value)}
              placeholder='Back of card'
              className='min-h-20'
            />
          </div>
        </div>
      </div>
      <div className='space-y-4 pt-4 border-t'>
        <h3 className='text-sm font-medium'>Select Deck</h3>
        <div className='flex items-center gap-4'>
          <DeckSelection initialDeckId={deckId} onDeckSelect={setDeckId} />
          <Button onClick={handleSubmit} disabled={isLoading || deckId === '-1'}>
            Save
          </Button>
        </div>
      </div>
    </div>
  );
};

export default GenericFlashcardPreview;
