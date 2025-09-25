import { MODEL_NAMES } from './model-names';
import { CONJUGATION_MODEL_TEMPLATE } from './template/conjugation-template';
import { INFLECTION_MODEL_TEMPLATE } from '@/anki-connect/model/template/inflection-template';
import { BASIC_MODEL_TEMPLATE } from '@/anki-connect/model/template/basic-template';
import { DECLENSION_MODEL_TEMPLATE } from '@/anki-connect/model/template/declension-template';

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

  const modelsToCheck = [
    { name: MODEL_NAMES.CONJUGATION, template: CONJUGATION_MODEL_TEMPLATE },
    { name: MODEL_NAMES.BASIC, template: BASIC_MODEL_TEMPLATE },
    { name: MODEL_NAMES.INFLECTION_GENERIC, template: INFLECTION_MODEL_TEMPLATE },
    { name: MODEL_NAMES.DECLENSION, template: DECLENSION_MODEL_TEMPLATE },
  ];

  for (const model of modelsToCheck) {
    if (!models.includes(model.name)) {
      await createModelFromTemplate(model.template);
    }
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
