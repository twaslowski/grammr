import React from 'react';
import { TextToSpeech } from '@/components/buttons/TextToSpeech';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Analysis } from '@/components/language/Analysis';
import { Translation } from '@/types/translation';
import GenericFlashcardExport from '@/flashcard/components/GenericFlashcardExport';

const TranslationCard: React.FC<{ translation: Translation }> = ({ translation }) => {
  const [analysis, setAnalysis] = React.useState(translation.analysis);
  return (
    <Card className='bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700'>
      <CardHeader>
        <CardTitle className='text-lg text-gray-900 dark:text-gray-100 flex justify-between items-center'>
          Translation
          <TextToSpeech text={translation.translation} />
          <GenericFlashcardExport
            layout='p-4 text-sm h-6'
            front={translation.translation}
            back={translation.source}
          />
        </CardTitle>
      </CardHeader>
      <CardContent>
        <Analysis analysis={analysis} onAnalysisUpdate={setAnalysis} />
      </CardContent>
    </Card>
  );
};

export default TranslationCard;
