import { render, screen } from '@testing-library/react';
import InflectionTable from '@/inflection/components/InflectionTable';
import { Paradigm } from '@/flashcard/types/paradigm';

describe('InflectionTable', () => {
  it('renders a loading spinner when no data is provided', () => {
    render(<InflectionTable inflections={null} isLoading={true} error={null} />);
    expect(screen.getByRole('status')).toBeInTheDocument();
  });

  it('renders an error message when error is provided', () => {
    render(
      <InflectionTable
        inflections={null}
        error={{ code: 500, message: 'some-message' }}
        isLoading={false}
      />,
    );
    expect(screen.getByText('some-message')).toBeInTheDocument();
  });

  it('renders a "not available" message when notAvailableInfo is provided', () => {
    render(
      <InflectionTable
        inflections={null}
        error={{ code: 422, message: 'Data not available' }}
        isLoading={false}
      />,
    );
    expect(screen.getByText('Data not available')).toBeInTheDocument();
  });

  it('renders the inflection table when valid inflections are provided', () => {
    const mockInflections: Paradigm = {
      lemma: 'dog',
      partOfSpeech: 'NOUN',
      paradigmId: '1',
      languageCode: 'en',
      inflections: [
        {
          lemma: 'dog',
          inflected: 'dog',
          features: [
            { type: 'CASE', value: 'NOM', fullIdentifier: 'Nominative' },
            { type: 'NUMBER', value: 'SING', fullIdentifier: 'Singular' },
          ],
        },
        {
          lemma: 'dog',
          inflected: 'dogs',
          features: [
            { type: 'CASE', value: 'NOM', fullIdentifier: 'Nominative' },
            { type: 'NUMBER', value: 'PLUR', fullIdentifier: 'Plural' },
          ],
        },
      ],
    };

    render(<InflectionTable inflections={mockInflections} error={null} isLoading={false} />);
    expect(screen.getByText('Nom')).toBeInTheDocument();
    expect(screen.getByText('dog')).toBeInTheDocument();
    expect(screen.getByText('dogs')).toBeInTheDocument();
  });
});
