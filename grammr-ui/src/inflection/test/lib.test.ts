import { findInflection, organizeInflectionTable } from '@/inflection/lib';
import {
  Inflection,
  InflectionTableData,
} from '@/inflection/types/inflections';

const nominative = { type: 'CASE', value: 'NOM', fullIdentifier: 'Nominative' };
const genitive = { type: 'CASE', value: 'GEN', fullIdentifier: 'Genitive' };
const singular = { type: 'NUMBER', value: 'SING', fullIdentifier: 'Singular' };
const plural = { type: 'NUMBER', value: 'PLUR', fullIdentifier: 'Plural' };

describe('organizeInflectionTable', () => {
  it('should organize inflections into a table structure', () => {
    const inflections: Inflection[] = [
      {
        lemma: 'Haus',
        inflected: 'Haus',
        features: [nominative, singular],
      },
      {
        lemma: 'Haus',
        inflected: 'Häuser',
        features: [nominative, plural],
      },
      {
        lemma: 'Haus',
        inflected: 'Hauses',
        features: [genitive, singular],
      },
    ];

    const expected: InflectionTableData = {
      NOM: {
        singular: 'Haus',
        plural: 'Häuser',
      },
      GEN: {
        singular: 'Hauses',
        plural: '',
      },
    };

    expect(
      organizeInflectionTable({
        inflections: inflections,
        partOfSpeech: 'NOUN',
        lemma: 'test',
      }),
    ).toEqual(expected);
  });

  it('should handle empty inflections array', () => {
    const inflections: Inflection[] = [];
    expect(
      organizeInflectionTable({
        inflections: inflections,
        partOfSpeech: 'NOUN',
        lemma: 'test',
      }),
    ).toEqual({});
  });

  it('should handle inflections without case or number features', () => {
    const inflections: Inflection[] = [
      {
        lemma: 'test',
        inflected: 'test',
        features: [
          { type: 'Gender', value: 'MASC', fullIdentifier: 'Masculine' },
        ],
      },
    ];

    expect(
      organizeInflectionTable({
        inflections: inflections,
        partOfSpeech: 'NOUN',
        lemma: 'test',
      }),
    ).toEqual({});
  });

  it('should handle verbs', () => {
    const inflections: Inflection[] = [
      {
        lemma: 'test',
        inflected: 'test',
        features: [
          { type: 'PERSON', value: 'FIRST', fullIdentifier: 'First person' },
          { type: 'NUMBER', value: 'SING', fullIdentifier: 'Singular' },
        ],
      },
      {
        lemma: 'test',
        inflected: 'test',
        features: [
          { type: 'PERSON', value: 'FIRST', fullIdentifier: 'First person' },
          { type: 'NUMBER', value: 'PLUR', fullIdentifier: 'Plural' },
        ],
      },
    ];

    const expected: InflectionTableData = {
      FIRST: {
        singular: 'test',
        plural: 'test',
      },
    };

    expect(
      organizeInflectionTable({
        inflections: inflections,
        partOfSpeech: 'VERB',
        lemma: 'test',
      }),
    ).toEqual(expected);
  });
});

describe('findInflections', () => {
  it('should find inflection with matching features', () => {
    const inflections: Inflection[] = [
      {
        lemma: 'test',
        inflected: 'test',
        features: [nominative, singular],
      },
      {
        lemma: 'test',
        inflected: 'test',
        features: [genitive, singular],
      },
    ];

    const features = [nominative, singular];
    expect(findInflection(inflections, features)).toEqual(inflections[0]);
  });

  it('should return undefined if no matching inflection is found', () => {
    const inflections: Inflection[] = [
      {
        lemma: 'test',
        inflected: 'test',
        features: [nominative, singular],
      },
    ];

    const features = [nominative, plural];
    expect(findInflection(inflections, features)).toBeUndefined();
  });

  it('should return undefined if no inflections are provided', () => {
    const inflections: Inflection[] = [];
    const features = [nominative, singular];
    expect(findInflection(inflections, features)).toBeUndefined();
  });
});
