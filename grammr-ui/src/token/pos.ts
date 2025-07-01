import { PartOfSpeechTag } from '@/types/pos';

export const getPosColor = (pos: string): string => {
  if (!pos) return 'text-gray-700';
  const posColors: { [key: string]: string } = {
    NOUN: 'bg-blue-50 hover:bg-blue-100',
    VERB: 'bg-green-50 hover:bg-green-100',
    ADJ: 'bg-purple-50 hover:bg-purple-100',
    // ADV: 'bg-yellow-50 hover:bg-yellow-100',
    // PRON: 'bg-pink-50 hover:bg-pink-100',
    DET: 'bg-gray-50 hover:bg-gray-100',
    // PREP: 'bg-orange-50 hover:bg-orange-100',
    // CONJ: 'bg-red-50 hover:bg-red-100',
  };

  return posColors[pos.toUpperCase()] || 'bg-white-100 hover:bg-gray-100';
};

export const getFullPos = (pos: string): string => {
  return PartOfSpeechTag[pos as keyof typeof PartOfSpeechTag] || 'Other';
};
