import { Book } from 'lucide-react';
import React from 'react';

import { Pos } from '@/components/language/Pos';
import Translation from '@/token/components/Translation';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { stringifyFeatures } from '@/token/feature';
import { getPosColor } from '@/token/pos';
import { TokenTranslation } from '@/types';
import TokenType from '@/token/types/tokenType';
import { useApi } from '@/hooks/useApi';
import { AnalysisV2 } from '@/types/analysis';

interface TokenProps {
  size?: 'sm' | 'md' | 'lg';
  context: string;
  token: TokenType;
  analysisId: string;
  onAnalysisUpdate: (analysis: AnalysisV2) => void;
  onShare(token: TokenType): void;
}

const Token: React.FC<TokenProps> = ({
  size,
  context,
  token,
  onShare,
  analysisId,
  onAnalysisUpdate,
}) => {
  const { index, text, translation, morphology } = token;
  const { request } = useApi();

  function needsSpacing(token: TokenType): boolean {
    return !['.', ',', '!', '?', ';', ':'].includes(token.text);
  }

  const onTranslation = async (translation: TokenTranslation): Promise<void> => {
    const result = await request<AnalysisV2>(`/api/v2/analysis/${analysisId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        ...token,
        translation: translation,
      }),
    });
    onAnalysisUpdate(result);
  };

  if (!morphology || Object.keys(morphology).length === 0) {
    return <span>{text}</span>;
  }

  return (
    <Popover>
      <PopoverTrigger>
        <span>
          <div className={`${size} ${getPosColor(morphology.pos)}`}>{text}</div>
        </span>
      </PopoverTrigger>
      <PopoverContent className='w-64'>
        <div className='flex items-center justify-between'>
          <p className='font-semibold'>{text}</p>
          <Book
            className='text-gray-500 hover:text-gray-700 cursor-pointer justify-end'
            onClick={() => onShare({ index, text, translation, morphology })}
          />
        </div>

        <div className='space-y-2 pb-2'>
          <Translation context={context} token={token} onTranslation={onTranslation} />
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
