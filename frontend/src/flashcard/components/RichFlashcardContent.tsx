import React from 'react';
import InflectionTable from '@/inflection/components/InflectionTable';
import { capitalize } from '@/lib/utils';
import { Paradigm } from '@/flashcard/types/paradigm';

interface FlashcardContentProps {
  front: string;
  back: string;
  paradigm: Paradigm;
}

const RichFlashcardContent: React.FC<FlashcardContentProps> = ({ back, paradigm }) => {
  return (
    <div className='text-center'>
      <p className='text-sm font-medium'>{back}</p>
      <p className='text-sm font-light pb-3'>{capitalize(paradigm.partOfSpeech)}</p>
      <InflectionTable textSize='text-sm' isLoading={false} inflections={paradigm} error={null} />
    </div>
  );
};

export default RichFlashcardContent;
