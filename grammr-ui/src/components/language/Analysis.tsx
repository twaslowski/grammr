import React from 'react';
import { AnalysisV2 } from '@/types/analysis';
import TokenType from '@/token/types/tokenType';
import Token from '@/token/components/Token';
import { useTokenPopover } from '@/context/TokenPopoverContext';

interface Props {
  analysis: AnalysisV2;
  onAnalysisUpdate: (analysis: AnalysisV2) => void;
}

export const Analysis: React.FC<Props> = ({ analysis, onAnalysisUpdate }) => {
  const { show } = useTokenPopover();

  return (
    <div className='flex flex-wrap'>
      {analysis.analysedTokens.map((token: TokenType) => (
        <Token
          size='sm'
          context={analysis.phrase}
          key={token.index}
          token={token}
          analysisId={analysis.analysisId}
          onShare={() => show(token, analysis.phrase, analysis.sourceLanguage)}
          onAnalysisUpdate={onAnalysisUpdate}
        />
      ))}
    </div>
  );
};
