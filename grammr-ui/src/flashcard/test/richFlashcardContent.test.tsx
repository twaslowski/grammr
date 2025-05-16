import { render } from '@testing-library/react';
import RichFlashcardContent from '../component/RichFlashcardContent';

jest.mock('@/inflection/InflectionTable', () => jest.fn(() => <div>Mocked InflectionTable</div>));

describe('FlashcardContent', () => {
  const mockToken = {
    translation: { translation: 'example translation' },
    morphology: { pos: 'noun' },
  };

  const mockInflections = {
    someKey: 'someValue',
  };

  it('renders translation and POS correctly', () => {
    const { getByText } = render(
      // @ts-expect-error: Mock object does not match the exact shape of TokenType
      <RichFlashcardContent inflections={null} token={mockToken} />,
    );

    expect(getByText('example translation')).toBeInTheDocument();
    expect(getByText('Noun')).toBeInTheDocument();
  });

  it('renders InflectionTable when inflections are provided', () => {
    const { getByText } = render(
      // @ts-expect-error: Mock objects only represent partial shapes of their respective types
      <RichFlashcardContent inflections={mockInflections} token={mockToken} />,
    );

    expect(getByText('Mocked InflectionTable')).toBeInTheDocument();
  });

  it('does not render InflectionTable when inflections are null', () => {
    const { queryByText } = render(
      // @ts-expect-error: Mock object does not match the exact shape of TokenType
      <RichFlashcardContent inflections={null} token={mockToken} />,
    );

    expect(queryByText('Mocked InflectionTable')).not.toBeInTheDocument();
  });
});
