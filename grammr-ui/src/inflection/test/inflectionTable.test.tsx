import { render, screen } from '@testing-library/react';
import InflectionTable from '@/inflection/InflectionTable';
import { Inflections } from '@/inflection/types/inflections';

describe('InflectionTable', () => {
  it('renders a loading spinner when no data is provided', () => {
    render(
      <InflectionTable
        inflections={null}
        error={null}
        notAvailableInfo={null}
      />,
    );
    expect(screen.getByRole('status')).toBeInTheDocument();
  });

  it('renders an error message when error is provided', () => {
    render(
      <InflectionTable
        inflections={null}
        error='Error loading data'
        notAvailableInfo={null}
      />,
    );
    expect(screen.getByText('Error loading data')).toBeInTheDocument();
  });

  it('renders a "not available" message when notAvailableInfo is provided', () => {
    render(
      <InflectionTable
        inflections={null}
        error={null}
        notAvailableInfo='Data not available'
      />,
    );
    expect(screen.getByText('Data not available')).toBeInTheDocument();
  });

  it('renders the inflection table when valid inflections are provided', () => {
    const mockInflections: Inflections = {
      lemma: 'dog',
      partOfSpeech: 'NOUN',
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

    render(
      <InflectionTable
        inflections={mockInflections}
        error={null}
        notAvailableInfo={null}
      />,
    );
    expect(screen.getByText('Nom')).toBeInTheDocument();
    expect(screen.getByText('dog')).toBeInTheDocument();
    expect(screen.getByText('dogs')).toBeInTheDocument();
  });
});
