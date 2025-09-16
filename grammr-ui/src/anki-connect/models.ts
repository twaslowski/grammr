import {
  VERSIONED_MODEL_NAMES,
  MODEL_VERSIONS,
  MODEL_MIGRATION_STATUS,
} from '@/constant/constants';
import {
  CONJUGATION_MODEL_TEMPLATE,
  CONJUGATION_CLOZE_MODEL_TEMPLATE,
} from './conjugation-templates';

// Model status tracking
interface ModelStatus {
  exists: boolean;
  version?: string;
  migrationStatus: string;
}

// Model registry for easy management
const MODEL_REGISTRY = {
  conjugation: CONJUGATION_MODEL_TEMPLATE,
  conjugationCloze: CONJUGATION_CLOZE_MODEL_TEMPLATE,
  // Add other models here as they're created
};

export const checkModelStatus = async (modelName: string): Promise<ModelStatus> => {
  try {
    const response = await fetch('http://localhost:8765', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        action: 'modelNames',
        version: 6,
      }),
    });

    if (!response.ok) {
      throw new Error('AnkiConnect is not running or not reachable.');
    }

    const data = await response.json();
    if (data.error) {
      throw new Error(data.error);
    }

    const models = data.result as string[];
    const versionedModelName =
      VERSIONED_MODEL_NAMES[modelName as keyof typeof VERSIONED_MODEL_NAMES];

    // Check if the current versioned model exists
    if (models.includes(versionedModelName)) {
      return {
        exists: true,
        version: MODEL_VERSIONS[modelName as keyof typeof MODEL_VERSIONS],
        migrationStatus: MODEL_MIGRATION_STATUS.UP_TO_DATE,
      };
    }

    // Check if any older version exists
    const baseModelName = modelName.replace(/_(v\d+\.\d+\.\d+)$/, '');
    const olderVersions = models.filter(
      (m) => m.startsWith(baseModelName) && m !== versionedModelName,
    );

    if (olderVersions.length > 0) {
      return {
        exists: true,
        version: 'unknown',
        migrationStatus: MODEL_MIGRATION_STATUS.NEEDS_MIGRATION,
      };
    }

    return {
      exists: false,
      migrationStatus: MODEL_MIGRATION_STATUS.NOT_FOUND,
    };
  } catch (error) {
    console.error('Error checking model status:', error);
    throw error;
  }
};

export const precheckModels = async () => {
  const response = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'modelNames',
      version: 6,
    }),
  });

  if (!response.ok) {
    throw new Error('AnkiConnect is not running or not reachable.');
  }

  const data = await response.json();
  if (data.error) {
    throw new Error(data.error);
  }

  const models = data.result as string[];

  // Check conjugation models
  if (!conjugationModelExists(models)) {
    await createConjugationModel();
  }

  // Keep the existing inflection model check for backward compatibility
  if (!inflectionsModelExists(models)) {
    await createModel();
  }
};

export const createConjugationModel = async (): Promise<void> => {
  await createModelFromTemplate(CONJUGATION_MODEL_TEMPLATE);
};

export const createModelFromTemplate = async (template: any): Promise<void> => {
  const response = await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'createModel',
      version: 6,
      params: template,
    }),
  });

  if (!response.ok) {
    throw new Error('Failed to create model');
  }

  const data = await response.json();
  if (data.error) {
    throw new Error(data.error);
  }
};

// Keep existing createModel function for backward compatibility
export const createModel = async (): Promise<void> => {
  await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'createModel',
      version: 6,
      params: {
        modelName: VERSIONED_MODEL_NAMES.INFLECTION,
        inOrderFields: ['front', 'back', 'lemma', 'translation', 'partOfSpeech', 'inflections'],
        css: 'table {border-collapse: collapse; width: 100%; max-width: 400px; margin-top: 10px;} th, td {border: 1px solid #aaa; padding: 4px 6px; text-align: center; font-size: 0.9em;} th {background-color: #f0f0f0;} .lemma {font-size: 1.5em; font-weight: bold; margin-bottom: 10px;} .meta {margin-bottom: 10px; font-style: italic;} @media (max-width: 480px) { table {font-size: 0.8em;} th, td {padding: 3px;}}',
        isCloze: false,
        cardTemplates: [
          {
            Name: 'Inflection Table Card',
            Front: '<div class="lemma">{{front}}</div>',
            Back: "<div class=\"lemma\">{{front}}</div><div class=\"meta\">{{partOfSpeech}}</div><div><strong>Translation:</strong> {{back}}</div><div id=\"table-container\"></div><script>const dataField = `{{inflections}}`.trim();try {const data = JSON.parse(dataField);const numbers = Object.keys(data);const cases = Object.keys(data[numbers[0]]);let table = '<table>';table += '<tr><th>Case</th>';for (let num of numbers) {table += `<th>${num}</th>`;}table += '</tr>';for (let caseName of cases) {table += `<tr><th>${caseName}</th>`;for (let num of numbers) {table += `<td>${data[num][caseName]}</td>`;}table += '</tr>'; }table += '</table>';document.getElementById('table-container').innerHTML = table;} catch (e) {document.getElementById('table-container').innerHTML = '<em>Invalid inflection data</em>';}</script><hr>",
          },
        ],
      },
    }),
  });
};

// Helper functions
function conjugationModelExists(models: string[]): boolean {
  return models.includes(VERSIONED_MODEL_NAMES.CONJUGATION);
}

function inflectionsModelExists(models: string[]): boolean {
  return models.includes(VERSIONED_MODEL_NAMES.INFLECTION);
}

function declensionModelExists(models: string[]): boolean {
  return models.includes(VERSIONED_MODEL_NAMES.DECLENSION);
}
