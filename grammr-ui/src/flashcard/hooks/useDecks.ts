import { useEffect, useState } from 'react';
import { toast } from '@/hooks/use-toast';
import Deck from '@/flashcard/types/deck';

export const useDecks = () => {
  const [decks, setDecks] = useState<Deck[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    void fetchDecks();
  }, []);

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
    } catch (error) {
      toast({
        title: 'Error loading decks',
        description: 'Please try again later',
        variant: 'destructive',
      });
    } finally {
      setIsLoading(false);
    }
  };

  const addDeck = async (name: string, description: string) => {
    try {
      setIsLoading(true);
      const response = await fetch('/api/v1/anki/deck', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, description }),
        credentials: 'same-origin',
      });
      const newDeck = await response.json();
      setDecks((prevDecks) => [...prevDecks, newDeck]);
      return newDeck;
    } catch (error) {
      toast({
        title: 'Error creating deck',
        description: 'Please try again later',
        variant: 'destructive',
      });
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  return { decks, addDeck, isLoading };
};
