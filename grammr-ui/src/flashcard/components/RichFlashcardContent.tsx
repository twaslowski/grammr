import React from 'react';
import InflectionTable from '@/inflection/components/InflectionTable';
import TokenType from '@/token/types/tokenType';
import { Inflections } from '@/inflection/types/inflections';
import { capitalize } from '@/lib/utils';

interface FlashcardContentProps {
  inflections: Inflections | null;
  token: TokenType;
}

const RichFlashcardContent: React.FC<FlashcardContentProps> = ({ inflections, token }) => {
  return (
    <div className='text-center'>
      <p className='text-sm font-medium'>{token.translation.translation}</p>
      <p className='text-sm font-light pb-3'>{capitalize(token.morphology.pos)}</p>
      {inflections && (
        <InflectionTable
          textSize='text-sm'
          showHeader={false}
          inflections={inflections}
          error={null}
          notAvailableInfo={null}
        />
      )}
    </div>
  );
};

export default RichFlashcardContent;
