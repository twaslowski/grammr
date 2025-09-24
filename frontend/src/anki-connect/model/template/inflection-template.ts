import { MODEL_NAMES } from '../model-names';

// Model template definitions with dumb fields (no JS)
export const INFLECTION_MODEL_TEMPLATE = {
  modelName: MODEL_NAMES.INFLECTION_GENERIC,
  inOrderFields: [
    'front',
    'back', // translation
    'lemma',
    'table',
    'notes',
  ],
  css: `
    .card {
      font-family: Arial, sans-serif;
      max-width: 600px;
      margin: 0 auto;
      padding: 16px;
    }

    .header {
      text-align: center;
      margin-bottom: 12px;
    }

    .lemma {
      font-size: 1.8em;
      font-weight: bold;
      color: #2c3e50;
      margin-bottom: 4px;
    }

    .translation {
      font-size: 1.05em;
      color: #7f8c8d;
      margin-bottom: 4px;
    }

    .table {
      border-collapse: collapse;
      width: 100%;
      margin: 12px 0;
    }

    .table th,
    .table td {
      border: 1px solid #ddd;
      padding: 8px 6px;
      text-align: center;
    }

    .table th {
      background-color: #f6f8fa;
      font-weight: 700;
    }

    .notes {
      margin-top: 10px;
      padding: 8px;
      background-color: #ffffff;
      border-left: 4px solid #3498db;
      font-style: italic;
    }

    @media (max-width: 480px) {
      .lemma { font-size: 1.2em; }
      .table th, .table td { padding: 6px 4px; font-size: 0.9em; }
    }
  `,
  isCloze: false,
  cardTemplates: [
    {
      Name: 'Declension Front',
      Front: `
        <div class="card">
          <div class="header">
            <div class="lemma">{{front}}</div>
          </div>
        </div>
      `,
      Back: `
        <div class="card">
          <div class="header">
            <div class="lemma">{{lemma}}</div>
            <div class="translation">{{back}}</div>
          </div>
          
          {{table}}

          {{#notes}}
          <div class="notes"><strong>Notes:</strong> {{notes}}</div>
          {{/notes}}
        </div>
      `,
    },
  ],
};
