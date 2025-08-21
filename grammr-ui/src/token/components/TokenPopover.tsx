import { X } from 'lucide-react';
import React, { useCallback, useEffect, useState } from 'react';

import { TextToSpeech } from '@/components/buttons/TextToSpeech';
import { Pos } from '@/components/language/Pos';
import InflectionTable from '@/inflection/components/InflectionTable';
import Translation from '@/token/components/Translation';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { stringifyFeatures } from '@/token/feature';
import { TokenTranslation } from '@/types';
import TokenType from '@/token/types/tokenType';
import { useInflections } from '@/inflection/hooks/use-inflections';
import GenericFlashcardExport from '@/flashcard/components/GenericFlashcardExport';

interface TokenPopoverProps {
  onClose: () => void;
  context: string;
  languageCode: string;
  token: TokenType;
}

const TokenPopover: React.FC<TokenPopoverProps> = ({ onClose, context, token, languageCode }) => {
  const { isLoading, inflections, error } = useInflections(
    token.morphology.lemma,
    token.morphology.pos,
    languageCode,
  );

  const [_, setTranslationData] = useState<TokenTranslation | null>(null);

  useEffect(() => {
    if (token.translation) {
      setTranslationData(token.translation);
    }
  }, [token]);

  const tokenHasNoFeatures = (token: TokenType) => {
    return (
      !token.morphology.features ||
      token.morphology.features.filter((f) => f.type !== 'MISC').length === 0
    );
  };

  return (
    <div className='fixed inset-y-0 right-0 w-96 bg-white shadow-lg z-50 overflow-y-auto'>
      <Card className='h-full'>
        <CardHeader className='flex flex-row items-center justify-between border-b'>
          <CardTitle>Word Details</CardTitle>
          <Button variant='ghost' size='icon' onClick={onClose}>
            <X className='h-5 w-5' />
          </Button>
        </CardHeader>
        <CardContent className='p-6 space-y-4'>
          <div className='space-y-1'>
            <div className='flex items-center space-x-2'>
              <p className='text-2xl font-bold'>{token.text}</p>
              <TextToSpeech text={token.text} />
            </div>

            <div className='border-b pb-2'>
              <Pos pos={token.morphology.pos} className='text-gray-600 text-lg' />
              <p className='text-gray-600 text-md'>Base form: {token.morphology.lemma}</p>
              {!tokenHasNoFeatures(token) && (
                <p className='text-gray-600 text-md'>
                  {stringifyFeatures(token.morphology.pos, token.morphology.features)}
                </p>
              )}
            </div>

            <div className='py-2 border-b'>
              <Translation context={context} token={token} onTranslation={() => {}} />
            </div>

            <div className='py-2 border-b'>
              <InflectionTable inflections={inflections} isLoading={isLoading} error={error} />
            </div>

            <div className='py-2'>
              <GenericFlashcardExport
                paradigm={inflections}
                front={token.text}
                back={token.translation.translation}
              />
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};
export default TokenPopover;
