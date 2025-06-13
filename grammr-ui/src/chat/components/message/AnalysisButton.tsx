import React from 'react';
import { BookOpenCheck } from 'lucide-react';
import { useLanguage } from '@/context/LanguageContext';
import { Analysis } from '@/types/analysis';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import { Message } from '@/chat/types/message';

export const AnalysisButton: React.FC<{
  message: Message;
  onAnalysis: (result: Analysis) => void;
}> = ({ message, onAnalysis }) => {
  const [loading, setLoading] = React.useState(false);
  const { languageSpoken, languageLearned } = useLanguage();

  const handleAnalyzeGrammar = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/v1/analysis', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          phrase: message.content,
          userLanguageSpoken: languageSpoken,
          userLanguageLearned: languageLearned,
          performSemanticTranslation: false,
        }),
      });

      const data = (await response.json()) as Analysis;
      if (onAnalysis) {
        onAnalysis(data);
        // editMessage(message.id, {
        //   ...message,
        //   analysis: data,
        // });
      }
    } catch (error) {
      console.error('Analysis failed', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <LoadingSpinner message='' size={4} />;
  }

  return (
    <button
      onClick={handleAnalyzeGrammar}
      className='text-gray-500 hover:text-blue-600 transition'
      title='Analyze Grammar'
      disabled={loading}
    >
      <BookOpenCheck size={18} />
    </button>
  );
};
