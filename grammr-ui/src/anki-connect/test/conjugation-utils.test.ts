import { getFormFromInflections, FeatureIdentifier } from '../model/conjugation-utils';
import { Feature, FeatureType, Person, Number as Num } from '@/types/feature';
import { Inflection } from '@/inflection/types/inflections';

describe('getFormFromInflections', () => {
  it('returns the inflected form when features match exactly', () => {
    const inflections: Inflection[] = [
      {
        lemma: 'sein',
        inflected: 'bin',
        features: [
          {
            type: FeatureType.PERSON,
            value: Person.FIRST,
            fullIdentifier: 'First person',
          } as Feature,
          { type: FeatureType.NUMBER, value: Num.SINGULAR, fullIdentifier: 'Singular' } as Feature,
        ],
      },
      {
        lemma: 'sein',
        inflected: 'bist',
        features: [
          {
            type: FeatureType.PERSON,
            value: Person.SECOND,
            fullIdentifier: 'Person=Second person',
          } as Feature,
          {
            type: FeatureType.NUMBER,
            value: Num.SINGULAR,
            fullIdentifier: 'Number=Singular',
          } as Feature,
        ],
      },
    ];

    const features: FeatureIdentifier[] = [Person.FIRST, Num.SINGULAR];

    const form = getFormFromInflections(inflections, features);
    expect(form).toBe('bin');
  });

  it('returns undefined when no inflection matches', () => {
    const inflections: Inflection[] = [
      {
        lemma: 'sein',
        inflected: 'ist',
        features: [
          {
            type: FeatureType.PERSON,
            value: Person.THIRD,
            fullIdentifier: 'Third person',
          } as Feature,
          { type: FeatureType.NUMBER, value: Num.SINGULAR, fullIdentifier: 'Singular' } as Feature,
        ],
      },
    ];

    const features: FeatureIdentifier[] = [Person.FIRST, Num.PLURAL];

    const form = getFormFromInflections(inflections, features);
    expect(form).toBeUndefined();
  });
});
