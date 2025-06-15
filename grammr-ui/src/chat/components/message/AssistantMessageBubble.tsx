import React, { useEffect } from 'react';
import Image from 'next/image';
import { AnalysisV2 } from '@/types/analysis';
import { Message } from '@/chat/types/message';
import { TextToSpeech } from '@/components/buttons/TextToSpeech';
import { AnalysisButton } from '@/chat/components/message/AnalysisButton';
import { useApi } from '@/hooks/useApi';
import { Analysis } from '@/components/language/Analysis';

export const AssistantMessageBubble: React.FC<{ message: Message }> = ({ message }) => {
  const [analysis, setAnalysis] = React.useState<AnalysisV2 | null>(null);
  const { request, error } = useApi();

  useEffect(() => {
    const fetchAnalysis = async () => {
      if (message.analysisId && !analysis) {
        try {
          const fetched = await request<AnalysisV2>(`/api/v2/analysis/${message.analysisId}`);
          setAnalysis(fetched);
        } catch (err) {
          console.error(`Failed to fetch analysis for ID ${message.analysisId}`, err);
        }
      }
    };

    void fetchAnalysis();
  }, [message.analysisId, request, analysis]);

  const onAnalysis = (analysis: AnalysisV2): void => {
    setAnalysis(analysis);

    const chatId = message.chatId;
    const messageId = message.id;
    request(
      `/api/v2/chat/${chatId}/messages/${messageId}`,
      {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          analysisId: analysis.analysisId,
        }),
      },
      'void',
    );
    if (error) {
      console.error(error);
    }
  };

  return (
    <div className='flex justify-start gap-2'>
      <div className='w-12 h-12 flex items-center justify-center rounded-full border'>
        <Image src={'/images/mascot.png'} alt={'mascot'} width={40} height={40} />
      </div>
      <div className='rounded-2xl border border-gray-300 p-2 max-w-[75%] text-gray-800 bg-white'>
        {analysis === null && (
          <div className='flex gap-2'>
            <p className='whitespace-pre-wrap break-words p-2'>{message.content}</p>
          </div>
        )}
        {analysis && (
          <div className='pb-1'>
            <Analysis analysis={analysis} onAnalysisUpdate={setAnalysis} />
          </div>
        )}
        <div className='border-t border-gray-300 w-full flex justify-end pt-2 pr-2 gap-2'>
          <TextToSpeech className='text-gray-600 hover:text-gray-800' text={message.content} />
          <AnalysisButton
            className='text-gray-600 hover:text-gray-800'
            message={message}
            onAnalysis={onAnalysis}
          />
        </div>
      </div>
    </div>
  );
};
