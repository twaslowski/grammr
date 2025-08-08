import Deck from '@/deck/types/deck';

export async function createDeck(deck: Deck) {
  await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'createDeck',
      version: 6,
      params: {
        deck: deck.name,
      },
    }),
  });
}
