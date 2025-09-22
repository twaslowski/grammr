import { MODEL_NAMES } from '../model-names';

// Model template definitions with dumb fields (no JS)
export const CONJUGATION_MODEL_TEMPLATE = {
  modelName: MODEL_NAMES.CONJUGATION,
  inOrderFields: [
    'front',
    'back', // translation
    'lemma',
    'firstPersonSingular',
    'secondPersonSingular',
    'thirdPersonSingular',
    'firstPersonPlural',
    'secondPersonPlural',
    'thirdPersonPlural',
    'notes',
  ],
  css: `
    .conjugation-card {
      font-family: Arial, sans-serif;
      max-width: 600px;
      margin: 0 auto;
      padding: 16px;
    }

    .verb-header {
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

    .conjugation-table {
      border-collapse: collapse;
      width: 100%;
      margin: 12px 0;
    }

    .conjugation-table th,
    .conjugation-table td {
      border: 1px solid #ddd;
      padding: 8px 6px;
      text-align: center;
    }

    .conjugation-table th {
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
      .conjugation-table th, .conjugation-table td { padding: 6px 4px; font-size: 0.9em; }
    }
  `,
  isCloze: false,
  cardTemplates: [
    {
      Name: 'Conjugation Front',
      Front: `
        <div class="conjugation-card">
          <div class="verb-header">
            <div class="lemma">{{front}}</div>
          </div>
        </div>
      `,
      Back: `
        <div class="conjugation-card">
          <div class="verb-header">
            <div class="lemma">{{lemma}}</div>
            <div class="translation">{{back}}</div>
          </div>

          <table class="conjugation-table">
            <tr>
              <th>Person</th>
              <th>Singular</th>
              <th>Plural</th>
            </tr>
            <tr>
              <th>1st</th>
              <td>{{firstPersonSingular}}</td>
              <td>{{firstPersonPlural}}</td>
            </tr>
            <tr>
              <th>2nd</th>
              <td>{{secondPersonSingular}}</td>
              <td>{{secondPersonPlural}}</td>
            </tr>
            <tr>
              <th>3rd</th>
              <td>{{thirdPersonSingular}}</td>
              <td>{{thirdPersonPlural}}</td>
            </tr>
          </table>

          {{#notes}}
          <div class="notes"><strong>Notes:</strong> {{notes}}</div>
          {{/notes}}
        </div>
      `,
    },
  ],
};
