# Anki Connect Package

This package provides a TypeScript interface for interacting with Anki through
the [AnkiConnect](https://ankiweb.net/shared/info/2055492159) add-on.
It handles deck creation, note management, and model validation for the Grammr application.

## Prerequisites

1. **Anki Desktop** must be installed and running
2. **AnkiConnect add-on** must be installed in Anki

- Install from: https://ankiweb.net/shared/info/2055492159
- Default port: 8765

## Package Structure

```
anki-connect/
├── README.md                    # This file
├── index.ts                     # Main exports
├── types.ts                     # Type definitions
├── util.ts                      # Connection utilities
├── deck.ts                      # Deck management
├── notes.ts                     # Note CRUD operations
├── models.ts                    # Model management
├── conjugation-templates.ts     # Model templates
├── conjugation-utils.ts         # Conjugation helpers
└── test-runner.ts              # Manual testing utility
```

## Core Functionality

### Connection Management

- **`precheckAnkiConnect()`** - Verify AnkiConnect is running and accessible

### Deck Management

- **`createDeck(name: string)`** - Create a new deck (idempotent)

### Note Operations

- **`createNotes(notes: Note[])`** - Batch create notes
- **`findNoteByFront(deckName: string, front: string)`** - Find note by front field
- **`updateNote(noteId: number, note: Note)`** - Update a single note
- **`updateNotes(notes: Note[])`** - Batch update notes
- **`deleteNotes(notes: Note[])`** - Batch delete notes

### Model Management

- **`precheckModels()`** - Ensure required models exist
- **`checkModelStatus(modelName: string)`** - Check model version and migration status
- **`createConjugationModel()`** - Create conjugation-specific models

## Usage Examples

### Basic Setup

```typescript
import { precheckAnkiConnect, precheckModels, createDeck } from '@/anki-connect';

// Verify connection and setup
await precheckAnkiConnect();
await precheckModels();
await createDeck('My Learning Deck');
```

### Creating Notes

```typescript
import { createNotes } from '@/anki-connect';
import { Note } from '@/deck/types/note';

const notes: Note[] = [
  {
    id: 'note-1',
    deckName: 'German Vocabulary',
    modelName: 'Basic',
    fields: {
      front: 'Hallo',
      back: 'Hello',
    },
    tags: ['greetings'],
  },
];

const result = await createNotes(notes);
console.log('Created notes:', result.successfulSyncs);
console.log('Failed notes:', result.failedSyncs);
```

### Finding and Updating Notes

```typescript
import { findNoteByFront, updateNote } from '@/anki-connect';

// Find a note by its front field
const noteId = await findNoteByFront('German Vocabulary', 'Hallo');

// Update the note
const updatedNote: Note = {
  id: 'note-1',
  deckName: 'German Vocabulary',
  modelName: 'Basic',
  fields: {
    front: 'Hallo',
    back: 'Hello (greeting)',
  },
  tags: ['greetings', 'basic'],
};

await updateNote(noteId, updatedNote);
```

## Manual Testing

The package includes a comprehensive test runner for manual testing and debugging:

### Quick Start

```bash
# Check if AnkiConnect is running
npx tsx src/anki-connect/test-runner.ts check-connection

# Run full test sequence
npx tsx src/anki-connect/test-runner.ts full-test
```

### Available Test Commands

| Command            | Description                         |
| ------------------ | ----------------------------------- |
| `check-connection` | Verify AnkiConnect is accessible    |
| `check-models`     | Validate and create required models |
| `create-test-deck` | Create a temporary test deck        |
| `create-test-note` | Create a test note                  |
| `find-test-note`   | Find the test note by front field   |
| `update-test-note` | Update the test note                |
| `delete-test-note` | Delete the test note                |
| `custom-query`     | Execute custom AnkiConnect queries  |
| `full-test`        | Run complete test sequence          |

### Custom Queries

Execute any AnkiConnect action directly:

```bash
# Get Anki version
npx tsx src/anki-connect/test-runner.ts custom-query version

# List all decks
npx tsx src/anki-connect/test-runner.ts custom-query deckNames

# Find notes in a specific deck
npx tsx src/anki-connect/test-runner.ts custom-query findNotes '{"query":"deck:MyDeck"}'

# Get note info
npx tsx src/anki-connect/test-runner.ts custom-query notesInfo '{"notes":[1234567890]}'
```

## Error Handling

All functions throw errors with descriptive messages:

```typescript
try {
  await precheckAnkiConnect();
} catch (error) {
  console.error('AnkiConnect not available:', error.message);
  // Handle connection error
}

try {
  const noteId = await findNoteByFront('MyDeck', 'NonexistentFront');
} catch (error) {
  console.error('Note not found:', error.message);
  // Handle missing note
}
```

## Types

### Core Types

```typescript
interface Note {
  id: string;
  deckName: string;
  modelName: string;
  fields: Record<string, string>;
  tags?: string[];
}

interface SyncResult {
  successfulSyncs: string[];
  failedSyncs: string[];
}

interface AnkiConnectResult {
  result: any;
  error: string | null;
}
```

## Model Management

The package automatically manages Anki note models:

### Supported Models

- **Conjugation Model** - For verb conjugation tables
- **Conjugation Cloze Model** - For cloze deletion exercises
- **Basic Inflection Model** - For noun/adjective inflections

### Model Versioning

Models are versioned to support migrations:

- Models include version numbers in their names
- Automatic detection of outdated models
- Migration status tracking

### Model Status Check

```typescript
import { checkModelStatus } from '@/anki-connect';

const status = await checkModelStatus('conjugation');
console.log('Model exists:', status.exists);
console.log('Model version:', status.version);
console.log('Migration status:', status.migrationStatus);
```

## AnkiConnect API Reference

This package uses AnkiConnect version 6. Key actions used:

| Action        | Purpose                   |
| ------------- | ------------------------- |
| `version`     | Check AnkiConnect version |
| `createDeck`  | Create new deck           |
| `addNotes`    | Add multiple notes        |
| `findNotes`   | Search for notes          |
| `updateNote`  | Update single note        |
| `deleteNotes` | Delete multiple notes     |
| `modelNames`  | List available models     |
| `createModel` | Create new note model     |

## Troubleshooting

### Common Issues

1. **"AnkiConnect is not running"**

- Ensure Anki Desktop is open
- Verify AnkiConnect add-on is installed
- Check if port 8765 is available

2. **"No note found"**

- Verify deck name is correct
- Check if front field text matches exactly
- Ensure note exists in the specified deck

3. **Model creation fails**

- Check if model name already exists
- Verify model template structure
- Ensure required fields are defined

### Debug Mode

Use the test runner to debug specific operations:

```bash
# Test each operation individually
npx tsx src/anki-connect/test-runner.ts check-connection
npx tsx src/anki-connect/test-runner.ts check-models
npx tsx src/anki-connect/test-runner.ts create-test-deck
```

## Configuration

### Default Settings

- **AnkiConnect URL**: `http://localhost:8765`
- **API Version**: 6
- **Default Model**: Basic (for simple notes)

### Environment Variables

Currently uses hardcoded localhost connection. Future versions may support:

- `ANKI_CONNECT_HOST`
- `ANKI_CONNECT_PORT`

## Contributing

When adding new functionality:

1. Add the function to the appropriate module (`deck.ts`, `notes.ts`, etc.)
2. Export it from `index.ts`
3. Add a test command to `test-runner.ts`
4. Update this README with usage examples
5. Include proper error handling and TypeScript types

## Dependencies

- **fetch** - HTTP requests to AnkiConnect
- **@/deck/types/note** - Note type definitions
- **@/constant/constants** - Model configuration

## License

Part of the Grammr project. See main project LICENSE for details.
