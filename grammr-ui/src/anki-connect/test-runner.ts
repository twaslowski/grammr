#!/usr/bin/env tsx
/**
 * Manual test runner for anki-connect functionality
 * Run with: npx tsx src/anki-connect/test-runner.ts
 */

import { precheckAnkiConnect } from './util';
import { precheckModels } from './models';
import { createDeck } from './deck';
import { createNotes, findNoteByFront, updateNote, deleteNotes } from './notes';
import { Note } from '@/deck/types/note';

// Test data
const TEST_DECK_NAME = 'test-deck';
const TEST_NOTE: Note = {
  id: 'test-note-1',
  deckName: TEST_DECK_NAME,
  modelName: 'Basic',
  fields: {
    front: 'Test Front',
    back: 'Test Back',
  },
  tags: ['test'],
};

interface TestCommand {
  name: string;
  description: string;
  execute: () => Promise<void>;
}

const commands: TestCommand[] = [
  {
    name: 'check-connection',
    description: 'Check if AnkiConnect is running and accessible',
    execute: async () => {
      console.log('Checking AnkiConnect connection...');
      await precheckAnkiConnect();
      console.log('✅ AnkiConnect is running and accessible');
    },
  },
  {
    name: 'check-models',
    description: 'Check and create required models',
    execute: async () => {
      console.log('Checking models...');
      await precheckModels();
      console.log('✅ Models are ready');
    },
  },
  {
    name: 'create-test-deck',
    description: 'Create a test deck',
    execute: async () => {
      console.log(`Creating test deck: ${TEST_DECK_NAME}`);
      await createDeck(TEST_DECK_NAME);
      console.log('✅ Test deck created');
    },
  },
  {
    name: 'create-test-note',
    description: 'Create a test note',
    execute: async () => {
      console.log('Creating test note...');
      const result = await createNotes([TEST_NOTE]);
      console.log('✅ Test note created:', result);
    },
  },
  {
    name: 'find-test-note',
    description: 'Find the test note by front field',
    execute: async () => {
      console.log('Finding test note...');
      const noteId = await findNoteByFront(TEST_DECK_NAME, TEST_NOTE.fields.front);
      console.log('✅ Found test note with ID:', noteId);
    },
  },
  {
    name: 'update-test-note',
    description: 'Update the test note',
    execute: async () => {
      console.log('Updating test note...');
      const noteId = await findNoteByFront(TEST_DECK_NAME, TEST_NOTE.fields.front);
      const updatedNote = {
        ...TEST_NOTE,
        fields: {
          ...TEST_NOTE.fields,
          back: 'Updated Test Back',
        },
      };
      const result = await updateNote(noteId, updatedNote);
      console.log('✅ Test note updated:', result);
    },
  },
  {
    name: 'delete-test-note',
    description: 'Delete the test note',
    execute: async () => {
      console.log('Deleting test note...');
      const result = await deleteNotes([TEST_NOTE]);
      console.log('✅ Test note deleted:', result);
    },
  },
  {
    name: 'custom-query',
    description: 'Execute a custom AnkiConnect query',
    execute: async () => {
      const action = process.argv[3] || 'version';
      const params = process.argv[4] ? JSON.parse(process.argv[4]) : {};

      console.log(`Executing custom query: ${action}`);
      console.log('Parameters:', params);

      const response = await fetch('http://localhost:8765', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          action,
          version: 6,
          params,
        }),
      });

      const data = await response.json();
      console.log('Response:', JSON.stringify(data, null, 2));
    },
  },
  {
    name: 'full-test',
    description: 'Run a complete test sequence',
    execute: async () => {
      console.log('Running complete test sequence...\n');

      const testSequence = [
        'check-connection',
        'check-models',
        'create-test-deck',
        'create-test-note',
        'find-test-note',
        'update-test-note',
        'delete-test-note',
      ];

      for (const testName of testSequence) {
        const command = commands.find((c) => c.name === testName);
        if (command) {
          try {
            await command.execute();
            console.log('');
          } catch (error) {
            console.error(`❌ Test failed: ${testName}`, error);
            break;
          }
        }
      }

      console.log('🎉 Full test sequence completed');
    },
  },
];

async function main() {
  const commandName = process.argv[2];

  if (!commandName) {
    console.log('Available commands:');
    commands.forEach((cmd) => {
      console.log(`  ${cmd.name.padEnd(20)} - ${cmd.description}`);
    });
    console.log('\nUsage:');
    console.log('  npx tsx src/anki-connect/test-runner.ts <command>');
    console.log('\nExamples:');
    console.log('  npx tsx src/anki-connect/test-runner.ts check-connection');
    console.log('  npx tsx src/anki-connect/test-runner.ts full-test');
    console.log('  npx tsx src/anki-connect/test-runner.ts custom-query deckNames');
    console.log(
      '  npx tsx src/anki-connect/test-runner.ts custom-query findNotes \'{"query":"deck:MyDeck"}\'',
    );
    return;
  }

  const command = commands.find((c) => c.name === commandName);
  if (!command) {
    console.error(`Unknown command: ${commandName}`);
    return;
  }

  try {
    await command.execute();
  } catch (error) {
    console.error(`❌ Command failed:`, error);
    process.exit(1);
  }
}

// Run if called directly
if (require.main === module) {
  main().catch(console.error);
}
