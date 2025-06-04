import { render, screen } from '@testing-library/react';
import { Suggestions } from '@/chat/components/Suggestions';

describe('Suggestions', () => {
  it('renders the correct number of suggestion buttons', () => {
    const n = 5;
    render(<Suggestions onSuggestionClickAction={() => {}} numberOfSuggestions={n} />);
    const buttons = screen.getAllByRole('button');
    expect(buttons).toHaveLength(n);
  });
});
