import { Note } from '@/deck/types/note';

/**
 * Finds a note by its front field in a specific deck.
 * This way we can avoid having to track note IDs, since front fields are unique per deck.
 *
 * @param deckName - The name of the deck to search in.
 * @param front - The front text to search for.
 * @returns The note ID if found.
 * @throws Error if no note is found or if Anki Connect returns an error.
 */

export async function findNoteByFront(deckName: string, front: string): Promise<number> {
  const response = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'findNotes',
      version: 6,
      params: {
        query: `deck:${deckName} front:${front}`,
      },
    }),
  });

  const data = (await response.json()) as AnkiConnectResult;
  if (data.error) {
    throw new Error(`Anki Connect error: ${data.error}`);
  } else if (data.result.length === 0) {
    throw new Error(`No note found in deck "${deckName}" with front "${front}"`);
  } else {
    return data.result[0];
  }
}

/**
 * Creates Notes in Anki using Anki Connect.
 * Notably, the deck is specified on the note itself, so we don't need to pass it here.
 *
 * @param notes - notes to create
 * @throws Error if no note is found or if Anki Connect returns an error.
 */
export async function createNotes(notes: Note[]): Promise<SyncResult> {
  const result = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'addNotes',
      version: 6,
      params: {
        notes: notes,
      },
    }),
  });
  const data = await result.json();
  if (data.error) {
    return {
      successfulSyncs: [],
      failedSyncs: notes.map((n) => n.id),
    };
  }
  return {
    successfulSyncs: notes.map((n) => n.id),
    failedSyncs: [],
  };
}

export async function updateNotes(notes: Note[]): Promise<SyncResult> {
  return {
    successfulSyncs: [],
    failedSyncs: [],
  };
}

export async function updateNote(noteId: number, note: Note): Promise<SyncResult> {
  const result = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'updateNote',
      version: 6,
      params: {
        note: {
          id: noteId,
          fields: note.fields,
        },
      },
    }),
  });
  const data = await result.json();
  if (data.error) {
    return {
      successfulSyncs: [],
      failedSyncs: [note.id],
    };
  } else {
    return {
      successfulSyncs: [note.id],
      failedSyncs: [],
    };
  }
}

/**
 * Deletes Notes in Anki using Anki Connect.
 * This is used to delete notes that are no longer present in the deck.

 * @param notes
 */
export async function deleteNotes(notes: Note[]): Promise<SyncResult> {
  const successfulSyncs: string[] = [];
  const failedSyncs: string[] = [];

  for (const note of notes) {
    try {
      const noteId = await findNoteByFront(note.deckName, note.fields.front);

      await fetch('http://localhost:8765', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          action: 'deleteNotes',
          version: 6,
          params: {
            notes: [noteId],
          },
        }),
      });
    } catch (err) {
      console.error(
        `Error finding note with front "${note.fields.front}" in deck "${note.deckName}":`,
        err,
      );
      failedSyncs.push(note.id);
      continue;
    }
    successfulSyncs.push(note.id);
  }
  return {
    successfulSyncs,
    failedSyncs,
  };
}
