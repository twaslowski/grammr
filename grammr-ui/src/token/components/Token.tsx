import { ExternalLink } from 'lucide-react';
import React, { useCallback, useState } from 'react';

import { Pos } from '@/components/language/Pos';
import Translation from '@/token/components/Translation';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { stringifyFeatures } from '@/token/feature';
import { getPosColor } from '@/token/pos';
import { TokenTranslation } from '@/types';
import TokenType from '@/token/types/tokenType';

interface TokenProps {
  context: string;
  token: TokenType;
  onShare(token: TokenType): void;
}

const Token: React.FC<TokenProps> = ({ context, token, onShare }) => {
  const { index, text, translation, morphology } = token;
  const [_, setTranslationData] = useState<TokenTranslation>(translation);

  const onTranslationLoaded = useCallback((translation: TokenTranslation) => {
    setTranslationData(translation);
  }, []);

  if (!morphology || Object.keys(morphology).length === 0) {
    return <span>{text}</span>;
  }

  return (
    <Popover>
      <PopoverTrigger>
        <span>
          <div className={`text-lg ${getPosColor(morphology.pos)}`}>{text}</div>
        </span>
      </PopoverTrigger>
      <PopoverContent className='w-64'>
        <div className='flex items-center justify-between'>
          <p className='font-semibold'>{text}</p>
          <ExternalLink
            className='text-gray-500 hover:text-gray-700 cursor-pointer justify-end'
            onClick={() => onShare({ index, text, translation, morphology })}
          />
        </div>

        <div className='space-y-2 pb-2'>
          <Translation context={context} token={token} onTranslationLoaded={onTranslationLoaded} />
        </div>
        <div className='border-t pt-2'>
          <Pos className='font-normal' pos={morphology.pos} />
          {text.toLowerCase() !== morphology.lemma.toLowerCase() && (
            <p className='text-sm text-gray-600'>From: {morphology.lemma}</p>
          )}
          <p className='text-sm text-gray-600'>
            {stringifyFeatures(morphology.pos, morphology.features)}
          </p>
        </div>
      </PopoverContent>
    </Popover>
  );
};

export default Token;
