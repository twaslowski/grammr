import React, { useState } from 'react';
import NewDeckDialog from '@/flashcard/component/deck/NewDeckDialog';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { useDecks } from '@/flashcard/hooks/useDecks';
import Deck from '@/flashcard/types/deck';

interface DeckSelectionProps {
  onDeckSelect: (deckId: number) => void;
}

const DeckSelection: React.FC<DeckSelectionProps> = ({ onDeckSelect }) => {
  const { decks, addDeck, isLoading } = useDecks();
  const [selectedDeck, setSelectedDeck] = useState<Deck | null>(null);
  const [showNewDeckDialog, setShowNewDeckDialog] = useState(false);

  const handleDeckChange = (deckId: string) => {
    if (deckId === '_createNewDeck') {
      setShowNewDeckDialog(true);
    } else {
      const deck = decks.find((d) => d.id.toString() === deckId) || null;
      setSelectedDeck(deck);
      if (deck) {
        onDeckSelect(deck.id);
      }
    }
  };

  const createNewDeck = async (name: string, description: string) => {
    try {
      const newDeck = await addDeck(name, description);
      setSelectedDeck(newDeck);
      onDeckSelect(newDeck.id);
      setShowNewDeckDialog(false);
    } catch {
      // Error handling is already in the hook
    }
  };

  return (
    <div>
      <Select
        value={selectedDeck ? selectedDeck.id.toString() : ''}
        onValueChange={handleDeckChange}
        disabled={isLoading}
      >
        <SelectTrigger className='w-full'>
          <SelectValue placeholder='Select a deck' />
        </SelectTrigger>
        <SelectContent>
          {decks.map((deck) => (
            <SelectItem key={deck.id} value={deck.id.toString()}>
              {deck.name}
            </SelectItem>
          ))}
          <SelectItem value='_createNewDeck' className='text-blue-600'>
            <div className='flex items-center gap-2'>Create new deck</div>
          </SelectItem>
        </SelectContent>
      </Select>

      <NewDeckDialog
        isOpen={showNewDeckDialog}
        onClose={() => setShowNewDeckDialog(false)}
        onCreate={createNewDeck}
        isLoading={isLoading}
      />
    </div>
  );
};

export default DeckSelection;
