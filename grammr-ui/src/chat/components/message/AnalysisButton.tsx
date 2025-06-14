import React from 'react';
import { BookOpenCheck } from 'lucide-react';
import { useLanguage } from '@/context/LanguageContext';
import { AnalysisV2 } from '@/types/analysis';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import { Message } from '@/chat/types/message';
import { useApi } from '@/hooks/useApi';

export const AnalysisButton: React.FC<{
  message: Message;
  onAnalysis: (result: AnalysisV2) => void;
  className?: string;
}> = ({ message, onAnalysis, className }) => {
  const { languageLearned } = useLanguage();
  const { request, isLoading, error } = useApi();

  const handleAnalyzeGrammar = async () => {
    try {
      const response = await request<AnalysisV2>('/api/v2/analysis', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          phrase: message.content,
          language: languageLearned,
        }),
      });
      onAnalysis(response);
    } catch (error) {
      console.error('Analysis failed', error);
    }
  };

  if (isLoading) {
    return <LoadingSpinner message='' size={4} />;
  }

  return (
    <button
      onClick={handleAnalyzeGrammar}
      className={`rounded-md text-gray-500 hover:text-gray-700 cursor-pointer transition-colors ${className}`}
      title='Analyze Grammar'
      disabled={isLoading}
    >
      <BookOpenCheck />
    </button>
  );
};
