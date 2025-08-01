import React from 'react';
import { AnalysisV2 } from '@/types/analysis';
import TokenType from '@/token/types/tokenType';
import Token from '@/token/components/Token';
import { useTokenPopover } from '@/context/TokenPopoverContext';

interface Props {
  analysis: AnalysisV2;
  onAnalysisUpdate: (analysis: AnalysisV2) => void;
  spacing?: 'none' | 'tight' | 'normal' | 'loose';
}

export const Analysis: React.FC<Props> = ({ analysis, onAnalysisUpdate, spacing = 'loose' }) => {
  const { show } = useTokenPopover();

  function shouldAddSpaceAfter(currentToken: TokenType, nextToken?: TokenType): boolean {
    const currentText = currentToken.text;
    const nextText = nextToken?.text;

    // No space after these punctuation marks
    const noSpaceAfter = ['\"'];
    if (noSpaceAfter.includes(currentText)) {
      return false;
    }

    // No space before these punctuation marks or closing quotes
    const noSpaceBefore = [
      '.',
      ',',
      '!',
      '!\"',
      '?',
      ';',
      ':',
      ')',
      ']',
      '].',
      '}',
      '"',
      "'",
      '\u201C',
      '\u201D',
      '\u2018',
      '\u2019',
    ];
    if (nextText && noSpaceBefore.includes(nextText)) {
      return false;
    }

    // No space after opening punctuation or opening quotes
    const noSpaceAfterOpening = ['(', '[', '{', '"', "'", '\u201C', '\u201D', '\u2018', '\u2019'];
    if (noSpaceAfterOpening.includes(currentText)) {
      return false;
    }

    // Special handling for contractions and hyphens
    if (
      nextText === "'" ||
      currentText === "'" ||
      nextText === '-' ||
      currentText === '-' ||
      nextText === '\u2013' ||
      currentText === '\u2013' ||
      nextText === '\u2014' ||
      currentText === '\u2014'
    ) {
      return false;
    }

    // Default: add space after regular words
    return true;
  }

  function getSpacingElement() {
    switch (spacing) {
      case 'none':
        return null;
      case 'tight':
        return <span className='mx-[0.175rem]'> </span>; // 0.75 (between 0.5 and 1)
      case 'normal':
        return ' '; // Just a regular space character
      case 'loose':
        return <span className='mx-1'> </span>;
      default:
        return ' ';
    }
  }

  return (
    <div className='flex flex-wrap'>
      {analysis.analysedTokens.map((token: TokenType, index: number) => {
        const nextToken = analysis.analysedTokens[index + 1];
        const shouldAddSpace = shouldAddSpaceAfter(token, nextToken);

        return (
          <React.Fragment key={token.index}>
            <Token
              size='sm'
              context={analysis.phrase}
              token={token}
              analysisId={analysis.analysisId}
              onShare={() => show(token, analysis.phrase, analysis.sourceLanguage)}
              onAnalysisUpdate={onAnalysisUpdate}
            />
            {shouldAddSpace && getSpacingElement()}
          </React.Fragment>
        );
      })}
    </div>
  );
};
