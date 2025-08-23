import { Feature } from '@/types/feature';
import {
  Inflection,
  InflectionsNotAvailableError,
  InflectionTableData,
} from '@/inflection/types/inflections';
import { Paradigm } from '@/flashcard/types/paradigm';

export const organizeInflectionTable = (inflections: Paradigm): InflectionTableData => {
  if (inflections.partOfSpeech === 'NOUN' || inflections.partOfSpeech === 'ADJ') {
    return organizeNounInflectionTable(inflections.inflections);
  } else {
    return organizeVerbInflectionTable(inflections.inflections);
  }
};

const organizeNounInflectionTable = (inflections: Inflection[]): InflectionTableData => {
  const table: InflectionTableData = {};

  const features = ['NOM', 'GEN', 'DAT', 'ACC', 'ABL', 'LOC'];
  features.forEach((feature) => {
    table[feature] = {
      singular:
        findInflection(inflections, [
          { type: 'CASE', value: feature, fullIdentifier: '' },
          {
            type: 'NUMBER',
            value: 'SING',
            fullIdentifier: '',
          },
        ])?.inflected || '',
      plural:
        findInflection(inflections, [
          { type: 'CASE', value: feature, fullIdentifier: '' },
          {
            type: 'NUMBER',
            value: 'PLUR',
            fullIdentifier: '',
          },
        ])?.inflected || '',
    };
  });

  // remove all entries where both singular and plural are empty
  Object.keys(table).forEach((key) => {
    if (!table[key].singular && !table[key].plural) {
      delete table[key];
    }
  });
  return table;
};

const organizeVerbInflectionTable = (inflections: Inflection[]): InflectionTableData => {
  const table: InflectionTableData = {};

  const type = 'PERSON';
  const features = ['FIRST', 'SECOND', 'THIRD'];
  features.forEach((feature) => {
    table[feature] = {
      singular:
        findInflection(inflections, [
          { type: type, value: feature, fullIdentifier: '' },
          {
            type: 'NUMBER',
            value: 'SING',
            fullIdentifier: '',
          },
        ])?.inflected || '',
      plural:
        findInflection(inflections, [
          { type: type, value: feature, fullIdentifier: '' },
          {
            type: 'NUMBER',
            value: 'PLUR',
            fullIdentifier: '',
          },
        ])?.inflected || '',
    };
  });

  // remove all entries where both singular and plural are empty
  Object.keys(table).forEach((key) => {
    if (!table[key].singular && !table[key].plural) {
      delete table[key];
    }
  });
  return table;
};

export const findInflection = (
  inflections: Inflection[],
  features: Feature[],
): Inflection | undefined => {
  return inflections.find((inflection) => {
    return features.every((feature) =>
      inflection.features.some((f) => f.value === feature.value && f.type === feature.type),
    );
  });
};

export const fetchInflections = async (
  lemma: string,
  pos: string,
  languageCode: string,
): Promise<Paradigm> => {
  const response = await fetch(`/api/v1/inflection`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      lemma: lemma,
      partOfSpeechTag: pos,
      languageCode: languageCode,
    }),
  });

  if (!response.ok) {
    if (response.status === 422) {
      const body = await response.json();
      throw new InflectionsNotAvailableError(body.message);
    } else {
      const body = await response.json();
      throw new Error(body.message);
    }
  }

  const data = await response.json();
  return data as Paradigm;
};
