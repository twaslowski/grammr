import { useEffect, useState } from 'react';
import { useApi } from '@/hooks/useApi';
import Deck from '@/deck/types/deck';

export const useDecks = () => {
  const { isLoading, error, request } = useApi();
  const [decks, setDecks] = useState<Deck[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      const result = await request<Deck[]>('/api/v1/deck');
      if (result) setDecks(result);
    };
    void fetchData();
  }, [request]);

  const addDeck = async (name: string, description: string) => {
    const newDeck = await request<Deck>('/api/v1/deck', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, description }),
    });
    if (newDeck) setDecks((prev) => [...prev, newDeck]);
    return newDeck;
  };

  return { decks, addDeck, isLoading, error };
};
