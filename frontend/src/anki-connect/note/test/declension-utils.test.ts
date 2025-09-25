import { extractCases } from '../declension-utils';
import { Feature, FeatureType, Case, Number as Num } from '@/types/feature';
import { Inflection } from '@/inflection/types/inflections';
import { Paradigm } from '@/flashcard/types/paradigm';

describe('Integration with getFormFromInflections', () => {
  it('correctly extracts case forms using the real utility function', () => {
    const mockInflections: Inflection[] = [
      {
        lemma: 'Buch',
        inflected: 'das Buch',
        features: [
          { type: FeatureType.CASE, value: Case.NOM, fullIdentifier: 'Nominative' } as Feature,
          { type: FeatureType.NUMBER, value: Num.SINGULAR, fullIdentifier: 'Singular' } as Feature,
        ],
      },
      {
        lemma: 'Buch',
        inflected: 'des Buches',
        features: [
          { type: FeatureType.CASE, value: Case.GEN, fullIdentifier: 'Genitive' } as Feature,
          { type: FeatureType.NUMBER, value: Num.SINGULAR, fullIdentifier: 'Singular' } as Feature,
        ],
      },
      {
        lemma: 'Buch',
        inflected: 'die Bücher',
        features: [
          { type: FeatureType.CASE, value: Case.NOM, fullIdentifier: 'Nominative' } as Feature,
          { type: FeatureType.NUMBER, value: Num.PLURAL, fullIdentifier: 'Plural' } as Feature,
        ],
      },
    ];

    const paradigm: Paradigm = {
      paradigmId: 'buch-paradigm',
      lemma: 'Buch',
      partOfSpeech: 'NOUN',
      languageCode: 'DE',
      inflections: mockInflections,
    };

    const result = extractCases(paradigm);

    expect(result.nominativeSingular).toBe('das Buch');
    expect(result.genitiveSingular).toBe('des Buches');
    expect(result.nominativePlural).toBe('die Bücher');
    expect(result.dativeSingular).toBe(''); // Not defined in our mock inflections
  });
});
