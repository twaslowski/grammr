import React from 'react';
import { render, fireEvent, screen, waitFor } from '@testing-library/react';
import DeckSelection from '@/deck/components/DeckSelection';
import { useDecks } from '@/deck/hooks/useDecks';
import { toast } from '@/hooks/use-toast';

jest.mock('@/deck/hooks/useDecks');
const mockedUseDecks = useDecks as jest.Mock;

jest.mock('@/hooks/use-toast');
const mockedToast = toast as jest.Mock;

describe('DeckSelection Component', () => {
  const decks = [
    { id: 1, name: 'Deck 1' },
    { id: 2, name: 'Deck 2' },
  ];

  beforeEach(() => {
    mockedUseDecks.mockReturnValue({
      decks,
      addDeck: jest.fn(),
      isLoading: false,
    });
  });

  it('renders deck options and the create new deck option', () => {
    render(<DeckSelection onDeckSelect={jest.fn()} />);

    // Check deck names and new deck option
    expect(screen.getByText('Select a deck')).toBeInTheDocument();
  });

  it('calls onDeckSelect when an existing deck is selected', () => {
    const onDeckSelect = jest.fn();
    render(<DeckSelection onDeckSelect={onDeckSelect} />);

    // Open the select dropdown by clicking the SelectTrigger text (placeholder)
    fireEvent.click(screen.getByText('Select a deck'));

    expect(screen.getByText('Deck 1')).toBeInTheDocument();
    expect(screen.getByText('Deck 2')).toBeInTheDocument();
    expect(screen.getByText('Create new deck')).toBeInTheDocument();

    // Simulate deck selection by clicking on existing deck option
    fireEvent.click(screen.getByText('Deck 1'));

    expect(onDeckSelect).toHaveBeenCalledWith(1);
  });

  it('should select a new deck after it has been created', async () => {
    const onDeckSelect = jest.fn();
    const addDeckMock = jest.fn().mockResolvedValue({ id: 3, name: 'New Deck' });
    mockedUseDecks.mockReturnValue({
      decks,
      addDeck: addDeckMock,
      isLoading: false,
    });

    render(<DeckSelection onDeckSelect={onDeckSelect} />);

    // Open the select dropdown
    fireEvent.click(screen.getByText('Select a deck'));
    // Click on the create new deck option
    fireEvent.click(screen.getByText('Create new deck'));

    // Simulate creating a new deck
    fireEvent.change(screen.getByPlaceholderText('Deck name'), { target: { value: 'New Deck' } });
    fireEvent.change(screen.getByPlaceholderText('Description'), {
      target: { value: 'Description' },
    });
    fireEvent.click(screen.getByText('Create'));

    await waitFor(() => {
      expect(onDeckSelect).toHaveBeenCalledWith(3);
    });
  });

  it('should toast an error if addDeck errors out', async () => {
    const onDeckSelect = jest.fn();

    const addDeckMock = jest.fn().mockRejectedValue(new Error('Failed to create deck'));
    mockedUseDecks.mockReturnValue({
      decks,
      addDeck: addDeckMock,
      isLoading: false,
    });

    render(<DeckSelection onDeckSelect={onDeckSelect} />);

    // Open the select dropdown
    fireEvent.click(screen.getByText('Select a deck'));
    // Click on the create new deck option
    fireEvent.click(screen.getByText('Create new deck'));

    // Simulate creating a new deck
    fireEvent.change(screen.getByPlaceholderText('Deck name'), { target: { value: 'New Deck' } });
    fireEvent.change(screen.getByPlaceholderText('Description'), {
      target: { value: 'Description' },
    });
    fireEvent.click(screen.getByText('Create'));

    await waitFor(() => {
      expect(mockedToast).toHaveBeenCalledWith(expect.objectContaining({ title: 'Error' }));
    });
  });
});
