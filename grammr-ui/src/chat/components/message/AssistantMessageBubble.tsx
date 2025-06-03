import React from 'react';
import { ChatMessage } from '@/chat/types/message';
import clsx from 'clsx';
import Image from 'next/image';
import { AnalysisButton } from '@/chat/components/message/AnalysisButton';
import TokenType from '@/token/types/tokenType';
import Token from '@/token/components/Token';
import Analysis from '@/types/analysis';
import { useTokenPopover } from '@/context/TokenPopoverContext';

export const AssistantMessageBubble: React.FC<{ message: ChatMessage }> = ({ message }) => {
  const [analysis, setAnalysis] = React.useState<Analysis | null>(null);
  const { show } = useTokenPopover();

  return (
    <div className='my-2 flex justify-start gap-2'>
      <div className='w-12 h-12 flex items-center justify-center rounded-full border'>
        <Image src={'/images/mascot.png'} alt={'mascot'} width={40} height={40} />
      </div>
      <div
        className={clsx(
          'rounded-2xl border border-gray-300 p-2 max-w-[75%] text-gray-800 bg-white',
        )}
      >
        {analysis === null && (
          <div className='flex gap-2'>
            <p className='whitespace-pre-wrap break-words py-1'>{message.content}</p>
            <AnalysisButton message={message} onAnalysis={setAnalysis} />
          </div>
        )}
        {analysis && (
          <div className='flex flex-wrap gap-1 py-1'>
            {analysis.analyzedTokens.map((token: TokenType) => (
              <Token
                size='sm'
                context={message.content}
                key={token.index}
                token={token}
                onShare={() => show(token, message.content, analysis.sourceLanguage)}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
