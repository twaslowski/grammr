import {
  VERSIONED_MODEL_NAMES,
  MODEL_VERSIONS,
  MODEL_MIGRATION_STATUS,
} from '@/constant/constants';

// Type definitions for conjugation data structure
export interface ConjugationData {
  persons: string[]; // ['1st', '2nd', '3rd']
  numbers: string[]; // ['singular', 'plural']
  forms: Record<string, Record<string, string>>; // {'singular': {'1st': 'form', '2nd': 'form', '3rd': 'form'}, 'plural': {...}}
}

// Model template definitions
export const CONJUGATION_MODEL_TEMPLATE = {
  modelName: VERSIONED_MODEL_NAMES.CONJUGATION,
  inOrderFields: [
    'lemma', // The base form of the verb
    'translation', // Translation in target language
    'tense', // Present, Past, Future, etc.
    'mood', // Indicative, Subjunctive, etc.
    'conjugations', // JSON data for the conjugation table
    'language', // Source language code
    'notes', // Additional notes
  ],
  css: `
    .conjugation-card {
      font-family: Arial, sans-serif;
      max-width: 500px;
      margin: 0 auto;
      padding: 20px;
    }
    
    .verb-header {
      text-align: center;
      margin-bottom: 20px;
    }
    
    .lemma {
      font-size: 2em;
      font-weight: bold;
      color: #2c3e50;
      margin-bottom: 5px;
    }
    
    .translation {
      font-size: 1.2em;
      color: #7f8c8d;
      margin-bottom: 5px;
    }
    
    .tense-mood {
      font-size: 1em;
      color: #3498db;
      font-style: italic;
    }
    
    .conjugation-table {
      border-collapse: collapse;
      width: 100%;
      margin: 20px 0;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    
    .conjugation-table th,
    .conjugation-table td {
      border: 1px solid #bdc3c7;
      padding: 12px 8px;
      text-align: center;
    }
    
    .conjugation-table th {
      background-color: #3498db;
      color: white;
      font-weight: bold;
    }
    
    .conjugation-table td {
      background-color: #ffffff;
      font-weight: 500;
    }
    
    .conjugation-table tr:nth-child(even) td {
      background-color: #f8f9fa;
    }
    
    .notes {
      margin-top: 15px;
      padding: 10px;
      background-color: #f1f2f6;
      border-left: 4px solid #3498db;
      font-style: italic;
    }
    
    .error-message {
      color: #e74c3c;
      font-style: italic;
      text-align: center;
      padding: 20px;
    }
    
    @media (max-width: 480px) {
      .conjugation-card {
        padding: 10px;
      }
      
      .lemma {
        font-size: 1.5em;
      }
      
      .conjugation-table th,
      .conjugation-table td {
        padding: 8px 4px;
        font-size: 0.9em;
      }
    }
  `,
  isCloze: false,
  cardTemplates: [
    {
      Name: 'Conjugation Front',
      Front: `
        <div class="conjugation-card">
          <div class="verb-header">
            <div class="lemma">{{lemma}}</div>
            <div class="translation">{{translation}}</div>
            <div class="tense-mood">{{tense}} {{mood}}</div>
          </div>
          <div style="text-align: center; color: #7f8c8d; font-size: 1.1em;">
            Show conjugations
          </div>
        </div>
      `,
      Back: `
        <div class="conjugation-card">
          <div class="verb-header">
            <div class="lemma">{{lemma}}</div>
            <div class="translation">{{translation}}</div>
            <div class="tense-mood">{{tense}} {{mood}}</div>
          </div>
          
          <div id="conjugation-container"></div>
          
          {{#notes}}
          <div class="notes">
            <strong>Notes:</strong> {{notes}}
          </div>
          {{/notes}}
          
          <script>
            (function() {
              const conjugationsField = \`{{conjugations}}\`.trim();
              const container = document.getElementById('conjugation-container');
              
              try {
                const data = JSON.parse(conjugationsField);
                
                if (!data.persons || !data.numbers || !data.forms) {
                  throw new Error('Invalid conjugation data structure');
                }
                
                let table = '<table class="conjugation-table">';
                
                // Header row
                table += '<tr><th>Person</th>';
                for (let number of data.numbers) {
                  table += \`<th>\${number.charAt(0).toUpperCase() + number.slice(1)}</th>\`;
                }
                table += '</tr>';
                
                // Data rows
                for (let person of data.persons) {
                  table += \`<tr><th>\${person}</th>\`;
                  for (let number of data.numbers) {
                    const form = data.forms[number] && data.forms[number][person] 
                      ? data.forms[number][person] 
                      : '—';
                    table += \`<td>\${form}</td>\`;
                  }
                  table += '</tr>';
                }
                
                table += '</table>';
                container.innerHTML = table;
                
              } catch (e) {
                container.innerHTML = '<div class="error-message">Error displaying conjugations: ' + e.message + '</div>';
              }
            })();
          </script>
        </div>
      `,
    },
  ],
};

export const CONJUGATION_CLOZE_MODEL_TEMPLATE = {
  ...CONJUGATION_MODEL_TEMPLATE,
  modelName: `${VERSIONED_MODEL_NAMES.CONJUGATION}_Cloze`,
  isCloze: true,
  cardTemplates: [
    {
      Name: 'Conjugation Cloze',
      Front: `
        <div class="conjugation-card">
          <div class="verb-header">
            <div class="lemma">{{lemma}}</div>
            <div class="translation">{{translation}}</div>
            <div class="tense-mood">{{tense}} {{mood}}</div>
          </div>
          
          <div id="conjugation-container"></div>
          
          <script>
            (function() {
              const conjugationsField = \`{{conjugations}}\`.trim();
              const container = document.getElementById('conjugation-container');
              
              try {
                const data = JSON.parse(conjugationsField);
                
                if (!data.persons || !data.numbers || !data.forms) {
                  throw new Error('Invalid conjugation data structure');
                }
                
                let table = '<table class="conjugation-table">';
                
                // Header row
                table += '<tr><th>Person</th>';
                for (let number of data.numbers) {
                  table += \`<th>\${number.charAt(0).toUpperCase() + number.slice(1)}</th>\`;
                }
                table += '</tr>';
                
                // Data rows with cloze deletions
                for (let person of data.persons) {
                  table += \`<tr><th>\${person}</th>\`;
                  for (let number of data.numbers) {
                    const form = data.forms[number] && data.forms[number][person] 
                      ? data.forms[number][person] 
                      : '—';
                    // Wrap forms in cloze deletions
                    const clozeForm = form !== '—' ? \`{{c1::\${form}}}\` : '—';
                    table += \`<td>\${clozeForm}</td>\`;
                  }
                  table += '</tr>';
                }
                
                table += '</table>';
                container.innerHTML = table;
                
              } catch (e) {
                container.innerHTML = '<div class="error-message">Error displaying conjugations: ' + e.message + '</div>';
              }
            })();
          </script>
        </div>
      `,
      Back: `
        <div class="conjugation-card">
          <div class="verb-header">
            <div class="lemma">{{lemma}}</div>
            <div class="translation">{{translation}}</div>
            <div class="tense-mood">{{tense}} {{mood}}</div>
          </div>
          
          <div id="conjugation-container"></div>
          
          {{#notes}}
          <div class="notes">
            <strong>Notes:</strong> {{notes}}
          </div>
          {{/notes}}
          
          <script>
            (function() {
              const conjugationsField = \`{{conjugations}}\`.trim();
              const container = document.getElementById('conjugation-container');
              
              try {
                const data = JSON.parse(conjugationsField);
                
                let table = '<table class="conjugation-table">';
                
                // Header row
                table += '<tr><th>Person</th>';
                for (let number of data.numbers) {
                  table += \`<th>\${number.charAt(0).toUpperCase() + number.slice(1)}</th>\`;
                }
                table += '</tr>';
                
                // Data rows
                for (let person of data.persons) {
                  table += \`<tr><th>\${person}</th>\`;
                  for (let number of data.numbers) {
                    const form = data.forms[number] && data.forms[number][person] 
                      ? data.forms[number][person] 
                      : '—';
                    table += \`<td>\${form}</td>\`;
                  }
                  table += '</tr>';
                }
                
                table += '</table>';
                container.innerHTML = table;
                
              } catch (e) {
                container.innerHTML = '<div class="error-message">Error displaying conjugations</div>';
              }
            })();
          </script>
        </div>
      `,
    },
  ],
};
