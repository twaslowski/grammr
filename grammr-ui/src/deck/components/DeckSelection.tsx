import React, { useState } from 'react';
import NewDeckDialog from '@/deck/components/NewDeckDialog';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { useDecks } from '@/deck/hooks/useDecks';
import Deck from '@/deck/types/deck';
import { toast } from '@/hooks/use-toast';

interface DeckSelectionProps {
  initialDeckId?: string;
  onDeckSelect: (deckId: string) => void;
}

const NEW_DECK_CREATION = '_createNewDeck';

const DeckSelection: React.FC<DeckSelectionProps> = ({
  initialDeckId,
  onDeckSelect,
}) => {
  const { decks, addDeck, isLoading } = useDecks();
  const [selectedDeckId, setSelectedDeckId] = useState<string | undefined>(initialDeckId);
  const [showNewDeckDialog, setShowNewDeckDialog] = useState(false);

  const deckMap = React.useMemo(() => {
    const map = new Map<string, Deck>();
    decks.forEach((deck) => map.set(deck.id, deck));
    return map;
  }, [decks]);

  React.useEffect(() => {
    setSelectedDeckId(initialDeckId);
  }, [initialDeckId]);

  const handleDeckChange = (deckId: string) => {
    if (deckId === NEW_DECK_CREATION) {
      setShowNewDeckDialog(true);
    } else {
      setSelectedDeckId(deckId);
      if (deckMap.has(deckId)) {
        onDeckSelect(deckId);
      }
    }
  };

  const createNewDeck = async (name: string, description: string) => {
    try {
      const newDeck = await addDeck(name, description);
      if (newDeck) {
        setSelectedDeckId(newDeck.id);
        onDeckSelect(newDeck.id);
      }
      setShowNewDeckDialog(false);
    } catch {
      toast({
        title: 'Error',
        description: 'There was an error creating the new deck.',
        variant: 'destructive',
      });
    }
  };

  return (
    <div>
      <Select value={selectedDeckId} onValueChange={handleDeckChange} disabled={isLoading}>
        <SelectTrigger className='w-full'>
          <SelectValue placeholder='Select a deck' />
        </SelectTrigger>
        <SelectContent>
          {decks.map((deck) => (
            <SelectItem key={deck.id} value={deck.id}>
              {deck.name}
            </SelectItem>
          ))}
          <SelectItem value={NEW_DECK_CREATION} className='text-blue-600'>
            <div className='flex items-center gap-2'>Create new deck</div>
          </SelectItem>
        </SelectContent>
      </Select>

      <NewDeckDialog
        isOpen={showNewDeckDialog}
        onClose={() => setShowNewDeckDialog(false)}
        onCreate={createNewDeck}
      />
    </div>
  );
};

export default DeckSelection;
