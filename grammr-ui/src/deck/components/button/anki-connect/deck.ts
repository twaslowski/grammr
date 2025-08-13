/**
 * Creates a new deck in Anki using Anki Connect.
 * This function is idempotent, so it will not throw an error if the deck already exists.
 *
 * @param name - Name of the deck to create.
 */
export async function createDeck(name: string): Promise<void> {
  await fetch(ANKI_CONNECT_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'createDeck',
      version: ANKI_CONNECT_VERSION,
      params: {
        deck: name,
      },
    }),
  });
}
