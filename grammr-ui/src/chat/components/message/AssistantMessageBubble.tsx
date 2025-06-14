import React from 'react';
import Image from 'next/image';
import TokenType from '@/token/types/tokenType';
import Token from '@/token/components/Token';
import {AnalysisV2} from '@/types/analysis';
import {useTokenPopover} from '@/context/TokenPopoverContext';
import {Message} from '@/chat/types/message';
import {TextToSpeech} from '@/components/buttons/TextToSpeech';
import {AnalysisButton} from "@/chat/components/message/AnalysisButton";

export const AssistantMessageBubble: React.FC<{ message: Message }> = ({message}) => {
  const [analysis, setAnalysis] = React.useState<AnalysisV2 | null>(null);
  const {show} = useTokenPopover();

  return (
      <div className='flex justify-start gap-2'>
        <div className='w-12 h-12 flex items-center justify-center rounded-full border'>
          <Image src={'/images/mascot.png'} alt={'mascot'} width={40} height={40}/>
        </div>
        <div className='rounded-2xl border border-gray-300 p-2 max-w-[75%] text-gray-800 bg-white'>
          {analysis === null && (
              <div className='flex gap-2'>
                <p className='whitespace-pre-wrap break-words p-2'>{message.content}</p>
              </div>
          )}
          {analysis && (
              <div className='flex flex-wrap gap-1 py-1'>
                {analysis.analysedTokens.map((token: TokenType) => (
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
          <div className='border-t border-gray-300 w-full flex justify-end pt-2 pr-2 gap-2'>
            <TextToSpeech className='text-gray-600 hover:text-gray-800' text={message.content}/>
            <AnalysisButton className='text-gray-600 hover:text-gray-800'
                            message={message}
                            onAnalysis={setAnalysis}/>
          </div>
        </div>
      </div>
  );
};
