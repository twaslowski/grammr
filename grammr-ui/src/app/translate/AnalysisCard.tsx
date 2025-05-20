import React from 'react';
import TTSPlayer from '@/components/buttons/TextToSpeech';
import GenericFlashcardExport from '@/flashcard/components/GenericFlashcardExport';
import Token from '@/token/components/Token';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import TokenType from '@/token/types/tokenType';
import Analysis from '@/types/analysis';

interface TranslationCardProps {
  analysis: Analysis;
  onTokenClick: (token: TokenType) => void;
}

const AnalysisCard: React.FC<TranslationCardProps> = ({ analysis, onTokenClick }) => {
  return (
    <Card className='bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700'>
      <CardHeader>
        <CardTitle className='text-lg text-gray-900 dark:text-gray-100 flex justify-between items-center'>
          Translation
          <TTSPlayer text={analysis.semanticTranslation.translatedPhrase} />
          <GenericFlashcardExport
            layout='p-4 text-sm h-6'
            front={analysis.semanticTranslation.sourcePhrase}
            back={analysis.semanticTranslation.translatedPhrase}
          />
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className='flex flex-wrap gap-2'>
          {analysis.analyzedTokens.map((token: TokenType) => (
            <Token
              context={analysis.semanticTranslation.translatedPhrase}
              key={token.index}
              token={token}
              onShare={() => onTokenClick(token)}
            />
          ))}
        </div>
      </CardContent>
    </Card>
  );
};

export default AnalysisCard;
