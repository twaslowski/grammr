import { RotateCw } from 'lucide-react';
import React, { useState } from 'react';

import DeckSelection from '@/deck/components/DeckSelection';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { Textarea } from '@/components/ui/textarea';
import TokenType from '@/token/types/tokenType';
import { useInflections } from '@/inflection/hooks/useInflections';
import RichFlashcardBack from '@/flashcard/components/RichFlashcardContent';
import { useLanguage } from '@/context/LanguageContext';

interface FlashcardPreviewProps {
  token: TokenType;
  onClose: () => void;
}

const TokenFlashcardPreview: React.FC<FlashcardPreviewProps> = ({ token, onClose }) => {
  const { languageLearned } = useLanguage();
  const [deckId, setDeckId] = useState(-1);
  const [front, setFront] = useState(token.morphology.lemma);
  const [activeCard, setActiveCard] = useState('front');

  const { inflections } = useInflections(
    token.morphology.lemma,
    token.morphology.pos,
    languageLearned,
  );

  const handleToggle = () => {
    if (activeCard === 'front') {
      setActiveCard('back');
    } else {
      setActiveCard('front');
    }
  };

  const createTokenFlashcard = async (
    deckId: number,
    token: TokenType,
    paradigmId: string | undefined,
  ) => {
    console.log(deckId, token, paradigmId);
    return await fetch('/api/v1/flashcard', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        deckId: deckId,
        question: token.morphology.lemma,
        answer: token.translation.translation,
        tokenPos: token.morphology.pos,
        paradigmId: paradigmId,
      }),
    });
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
        <Card className='w-full h-64 cursor-pointer relative' onClick={handleToggle}>
          <RotateCw className='h-4 w-4 absolute top-4 right-4' />
          <CardContent className='flex items-center justify-center h-full p-0'>
            {activeCard === 'front' && <p>{front}</p>}
            {activeCard === 'back' && (
              <div className='text-xl'>
                <RichFlashcardBack inflections={inflections} token={token} />
              </div>
            )}
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
              disabled={true}
              value={'{{translation}}\n' + '{{part of speech}} \n' + '{{inflection}} \n'}
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
          <Button
            onClick={() => {
              console.log(inflections);
              void createTokenFlashcard(deckId, token, inflections?.paradigmId);
              onClose();
            }}
            disabled={deckId === -1}
          >
            Save
          </Button>
        </div>
      </div>
    </div>
  );
};

export default TokenFlashcardPreview;
