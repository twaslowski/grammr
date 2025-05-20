import { ArrowLeftRight, RotateCw } from 'lucide-react';
import React, { useState } from 'react';

import DeckSelection from '@/deck/components/DeckSelection';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { Textarea } from '@/components/ui/textarea';
import { toast } from '@/hooks/use-toast';

interface FlashcardPreviewProps {
  initialFront: string;
  initialBack: string;
  onClose: () => void;
}

const GenericFlashcardPreview: React.FC<FlashcardPreviewProps> = ({
  initialFront,
  initialBack,
  onClose,
}) => {
  const [deckId, setDeckId] = useState(-1);
  const [isLoading, setIsLoading] = useState(false);
  const [front, setFront] = useState(initialFront);
  const [back, setBack] = useState(initialBack);
  const [activeCard, setActiveCard] = useState('front');

  // Toggle front and back of the flashcard without changing anything
  const handleToggle = () => {
    if (activeCard === 'front') {
      setActiveCard('back');
    } else {
      setActiveCard('front');
    }
  };

  // Switch front and back of the flashcard
  const handleSwitch = () => {
    const newFront = back;
    const newBack = front;
    setFront(newFront);
    setBack(newBack);
  };

  const createFlashcard = async () => {
    try {
      setIsLoading(true);
      const response = await fetch('/api/v1/flashcard', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          deckId: deckId,
          question: front,
          answer: back,
        }),
      });

      if (response.status !== 201) {
        throw new Error('Failed to create flashcard');
      }

      toast({
        title: 'Success',
        description: 'Flashcard created successfully',
      });
      onClose();
    } catch (error) {
      toast({
        title: 'Error creating flashcard',
        description: 'Please try again later',
        variant: 'destructive',
      });
    } finally {
      setIsLoading(false);
    }
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
            <div className='text-xl'>{activeCard === 'front' ? front : back}</div>
          </CardContent>
        </Card>
      </div>

      <div className='py-4'>
        <ArrowLeftRight
          onClick={handleSwitch}
          className='w-full text-gray-500 hover:text-gray-700 cursor-pointer transition-colors'
        />
        <p className='text-center text-sm text-gray-500'>Reverse front and back</p>
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
          <DeckSelection onDeckSelect={setDeckId} />
          <Button onClick={createFlashcard} disabled={isLoading || deckId === -1}>
            Save
          </Button>
        </div>
      </div>
    </div>
  );
};

export default GenericFlashcardPreview;
