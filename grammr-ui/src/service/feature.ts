import { Feature } from '@/types/feature';

export const isVerbLike = (pos: string): boolean => {
  return pos === 'VERB' || pos === 'AUX';
};

export const isNounLike = (pos: string): boolean => {
  return pos === 'NOUN' || pos === 'PRON' || pos === 'ADJ';
};

export const stringifyFeatures = (pos: string, features: Feature[]) => {
  const relevantFeatures = retrieveFeatures(pos, features);
  return relevantFeatures
    .map((feature) => feature.fullIdentifier.toLowerCase())
    .join(' ');
};

export const retrieveFeatures = (
  pos: string,
  features: Feature[],
): Feature[] => {
  if (!pos) return [];
  const result = [];
  if (isNounLike(pos)) {
    result.push(features.find((f) => f.type === 'CASE') as Feature);
    result.push(features.find((f) => f.type === 'NUMBER') as Feature);
    result.push(features.find((f) => f.type === 'GENDER') as Feature);
  } else if (isVerbLike(pos)) {
    result.push(features.find((f) => f.type === 'PERSON') as Feature);
    result.push(features.find((f) => f.type === 'NUMBER') as Feature);
    result.push(features.find((f) => f.type === 'TENSE') as Feature);
  }
  return result.filter((f) => f !== undefined);
};
