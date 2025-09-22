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
        <div className='flex items-center justify-between'>
          <div className='flex items-center gap-2'>
            <CardTitle className='text-lg text-gray-900 dark:text-gray-100'>Translation</CardTitle>
            <TextToSpeech text={translation.translation} />
          </div>
          <GenericFlashcardExport
            layout='p-4 text-sm h-6'
            front={translation.translation}
            back={translation.source}
            paradigm={null} // Whole phrase, not an individual word
          />
        </div>
      </CardHeader>
      <CardContent>
        {/* Translation Section */}
        <div className='mb-6'>
          <div className='flex flex-col gap-2 px-4'>
            <div>
              <span className='text-gray-500'>Original:</span>
              <span className='ml-2 font-medium'>{translation.source}</span>
            </div>
            <div>
              <span className='text-gray-500'>Translation:</span>
              <span className='ml-2 font-medium'>{translation.translation}</span>
            </div>
          </div>
        </div>
        {/* Analysis Section */}
        <div>
          <h3 className='text-md font-semibold mb-2 text-gray-800 dark:text-gray-200'>Analysis</h3>
          <Analysis analysis={analysis} onAnalysisUpdate={setAnalysis} />
        </div>
      </CardContent>
    </Card>
  );
};

export default TranslationCard;
