import { Info } from 'lucide-react';
import React from 'react';

import { capitalize } from '@/util';
import { PartOfSpeechTag } from '@/types/pos';

interface PosProps {
  pos: string;
  className?: string;
}

const getFullPos = (pos: string): string => {
  return PartOfSpeechTag[pos as keyof typeof PartOfSpeechTag] || 'Other';
};

const isSelfExplanatory = (pos: string): boolean => {
  return ['NOUN', 'ADJ', 'VERB', 'AUX', 'PRON'].includes(pos);
};

const getInfoLink = (pos: string): string => {
  return `https://universaldependencies.org/u/pos/${pos}.html`;
};

export const Pos: React.FC<PosProps> = ({ pos, className }) => {
  return (
    <div className='flex items-center'>
      <p className={className}>{capitalize(getFullPos(pos))}</p>
      {!isSelfExplanatory(pos) && (
        <a href={getInfoLink(pos)} target='_blank' rel='noreferrer noopener'>
          <Info className='ml-2 h-4 w-4 text-gray-500 hover:text-gray-700' />
        </a>
      )}
    </div>
  );
};
