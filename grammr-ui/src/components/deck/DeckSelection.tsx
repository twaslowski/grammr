import React, { useEffect, useState } from 'react';

import NewDeckDialog from '@/components/deck/NewDeckDialog';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { toast } from '@/hooks/use-toast';
import Deck from '@/types/deck';

interface DeckSelectionProps {
  onDeckSelect: (deckId: number) => void;
}

const DeckSelection: React.FC<DeckSelectionProps> = ({ onDeckSelect }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [decks, setDecks] = useState<Deck[]>([]);
  const [selectedDeck, setSelectedDeck] = useState<Deck | null>(null);
  const [showNewDeckDialog, setShowNewDeckDialog] = useState(false);

  useEffect(() => {
    fetchDecks();
  }, []);

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

  const fetchDecks = async () => {
    try {
      setIsLoading(true);
      const response = await fetch('/api/v1/anki/deck', {
        credentials: 'same-origin',
      });
      if (response.status === 401) {
        toast({
          title: 'Please log in',
          description: 'Please log in to save features',
          variant: 'destructive',
        });
        return;
      }
      const data = await response.json();
      setDecks(data);
      setIsLoading(false);
    } catch (error) {
      toast({
        title: 'Error loading decks',
        description: 'Please try again later',
        variant: 'destructive',
      });
      setIsLoading(false);
    }
  };

  const createNewDeck = async (name: string, description: string) => {
    try {
      setIsLoading(true);
      const response = await fetch('/api/v1/anki/deck', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, description }),
        credentials: 'same-origin',
      });
      const newDeck = await response.json();
      setDecks([...decks, newDeck]);
      setShowNewDeckDialog(false);
      setSelectedDeck(newDeck);
      onDeckSelect(newDeck.id);
    } catch (error) {
      toast({
        title: 'Error creating deck',
        description: 'Please try again later',
        variant: 'destructive',
      });
    } finally {
      setIsLoading(false);
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
          {decks.map((deck: Deck) => (
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
