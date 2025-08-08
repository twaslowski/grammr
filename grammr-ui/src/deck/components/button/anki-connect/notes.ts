export async function getNote(deckName: string, front: string): Promise<Note | null> {
  const response = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'findNotes',
      version: 6,
      params: {
        query: `deck:"${deckName} front:${front}"`,
      },
    }),
  });
  // todo properly parse response
  const data = await response.json();
  return null;
}

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
  } else {
    return {
      successfulSyncs: notes.map((n) => n.id),
      failedSyncs: [],
    };
  }
}

export async function updateNote(deckName: string, note: Note): Promise<SyncResult> {
  const result = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'updateNotes',
      version: 6,
      params: {
        note: {
          id: note.id,
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

export async function deleteNotes(noteIds: string[]): Promise<SyncResult> {
  const result = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'deleteNotes',
      version: 6,
      params: {
        notes: noteIds,
      },
    }),
  });
  const data = await result.json();
  if (data.error) {
    return {
      successfulSyncs: [],
      failedSyncs: data.error,
    };
  } else {
    return {
      successfulSyncs: data.result,
      failedSyncs: [],
    };
  }
}
