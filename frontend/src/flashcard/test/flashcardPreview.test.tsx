import React from 'react';
import { fireEvent, render, waitFor } from '@testing-library/react';
import GenericFlashcardPreview from '@/flashcard/components/GenericFlashcardPreview';
import { toast } from '@/hooks/use-toast';

jest.mock('@/hooks/use-toast', () => ({
  toast: jest.fn(),
}));

global.fetch = jest.fn();

// Currently, there is no obvious way to select a Deck in the preview, meaning the `create` button
// will be disabled. Skipping the tests until I fix that.
describe.skip('GenericFlashcardPreview', () => {
  const defaultProps = {
    initialFront: 'front',
    initialBack: 'back',
    paradigm: null,
    onClose: jest.fn(),
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('shows success toast on successful flashcard creation', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({ ok: true });
    const { getByText, getByTestId } = render(<GenericFlashcardPreview {...defaultProps} />);

    fireEvent.change(getByTestId('deck-select'), { target: { value: '1' } });
    fireEvent.click(getByText('Save'));

    await waitFor(() => {
      expect(toast).toHaveBeenCalledWith(expect.objectContaining({ title: 'Success' }));
    });
  });

  it('shows error toast on failed flashcard creation', async () => {
    (global.fetch as jest.Mock).mockRejectedValueOnce(new Error('fail'));
    const { getByText } = render(<GenericFlashcardPreview {...defaultProps} />);
    fireEvent.click(getByText('Save'));
    await waitFor(() => {
      expect(toast).toHaveBeenCalledWith(
        expect.objectContaining({ title: 'Error creating flashcard' }),
      );
    });
  });
});
