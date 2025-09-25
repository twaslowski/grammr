import { MODEL_NAMES } from '../model-names';

// Model template definitions with dumb fields (no JS)
export const BASIC_MODEL_TEMPLATE = {
  modelName: MODEL_NAMES.BASIC,
  inOrderFields: ['id', 'front', 'back', 'notes'],
  css: `
    .card {
      font-family: Arial, sans-serif;
      text-align: center;
      max-width: 600px;
      margin: 0 auto;
      padding: 16px;
    }
    
    .text {
      font-size: 1.8em;
      font-weight: bold;
      color: #2c3e50;
      margin-bottom: 4px;
    }
  `,
  isCloze: false,
  cardTemplates: [
    {
      Name: 'Front and Back',
      Front: `
        <div class="card">
            <div class="text">{{front}}</div>
        </div>
      `,
      Back: `
        <div class="card">
            <div class="text">{{front}}</div>
            <hr id=answer>
            <div class="text">{{back}}</div>
          </div>

          {{#notes}}
          <div class="notes"><strong>Notes:</strong> {{notes}}</div>
          {{/notes}}
        </div>
      `,
    },
  ],
};
