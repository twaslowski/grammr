import React from 'react';
import { ChatMessage } from '@/chat/types/message';
import clsx from 'clsx';
import Image from 'next/image';
import { AnalysisButton } from '@/chat/components/message/AnalysisButton';
import TokenType from '@/token/types/tokenType';
import Token from '@/token/components/Token';

export const AssistantMessageBubble: React.FC<{ message: ChatMessage }> = ({ message }) => {
  const [analysisResult, setAnalysisResult] = React.useState<TokenType[]>([]);

  return (
    <div className='my-2 flex justify-start'>
      <div className='w-8 h-8 flex items-center justify-center rounded-full border'>
        <Image src={'/images/mascot.png'} alt={'mascot'} width={32} height={32} />
      </div>
      <div className={clsx('rounded-2xl px-4 max-w-[75%] text-gray-800 bg-white')}>
        {analysisResult.length === 0 ? (
          <div className='flex'>
            <p className='whitespace-pre-wrap break-words py-1'>{message.content}</p>
            <AnalysisButton message={message} onAnalysisResult={setAnalysisResult} />
          </div>
        ) : (
          <div className='flex flex-wrap gap-1 py-1'>
            {analysisResult.map((token: TokenType) => (
              <Token
                size='sm'
                context={message.content}
                key={token.index}
                token={token}
                onShare={() => {}}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
