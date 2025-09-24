import { MODEL_NAMES } from './model-names';
import { CONJUGATION_MODEL_TEMPLATE } from './template/conjugation-template';
import { INFLECTION_MODEL_TEMPLATE } from '@/anki-connect/model/template/inflection-template';
import { BASIC_MODEL_TEMPLATE } from '@/anki-connect/model/template/basic-template';

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

  if (!basicModelExists(models)) {
    await createBasicModel();
  }

  if (!inflectionsModelExists(models)) {
    await createInflectionModel();
  }
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

export const createInflectionModel = async (): Promise<void> => {
  await createModelFromTemplate(INFLECTION_MODEL_TEMPLATE);
};

export const createConjugationModel = async (): Promise<void> => {
  await createModelFromTemplate(CONJUGATION_MODEL_TEMPLATE);
};

function conjugationModelExists(models: string[]): boolean {
  return models.includes(MODEL_NAMES.CONJUGATION);
}

export const createBasicModel = async (): Promise<void> => {
  await createModelFromTemplate(BASIC_MODEL_TEMPLATE);
};

function basicModelExists(models: string[]): boolean {
  return models.includes(MODEL_NAMES.BASIC);
}

function inflectionsModelExists(models: string[]): boolean {
  return models.includes(MODEL_NAMES.INFLECTION_GENERIC);
}
