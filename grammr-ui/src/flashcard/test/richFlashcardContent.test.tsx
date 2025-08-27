import { render } from '@testing-library/react';
import RichFlashcardContent from '@/flashcard/components/RichFlashcardContent';
import { Paradigm } from '@/flashcard/types/paradigm';

jest.mock('@/inflection/components/InflectionTable', () =>
  jest.fn(() => <div>Mocked InflectionTable</div>),
);

describe('FlashcardContent', () => {
  const paradigm: Paradigm = {
    paradigmId: '1',
    lemma: 'test',
    languageCode: 'en',
    partOfSpeech: 'noun',
    inflections: [
      {
        lemma: 'test',
        inflected: 'tests',
        features: [
          {
            type: 'number',
            value: 'plural',
            fullIdentifier: 'Plural',
          },
        ],
      },
    ],
  };

  it('renders InflectionTable when inflections are provided', () => {
    const { getByText } = render(
      <RichFlashcardContent paradigm={paradigm} front={'front'} back={'back'} />,
    );

    expect(getByText('Noun')).toBeInTheDocument();
    expect(getByText('back')).toBeInTheDocument();
  });
});
