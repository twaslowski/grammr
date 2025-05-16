import { PartOfSpeechTag } from '@/types/pos';

export const getPosColor = (pos: string): string => {
  if (!pos) return 'text-gray-700';
  const posColors: { [key: string]: string } = {
    NOUN: 'bg-blue-100 hover:bg-blue-200',
    VERB: 'bg-green-100 hover:bg-green-200',
    ADJ: 'bg-purple-100 hover:bg-purple-200',
    // ADV: 'bg-yellow-100 hover:bg-yellow-200',
    // PRON: 'bg-pink-100 hover:bg-pink-200',
    DET: 'bg-gray-100 hover:bg-gray-200',
    // PREP: 'bg-orange-100 hover:bg-orange-200',
    // CONJ: 'bg-red-100 hover:bg-red-200',
  };

  return posColors[pos.toUpperCase()] || 'bg-white-100 hover:bg-gray-100';
};

export const getFullPos = (pos: string): string => {
  return PartOfSpeechTag[pos as keyof typeof PartOfSpeechTag] || 'Other';
};
