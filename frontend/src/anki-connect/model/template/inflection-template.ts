import { MODEL_NAMES } from '../model-names';

export const INFLECTION_MODEL_TEMPLATE = {
  modelName: MODEL_NAMES.INFLECTION,
  inOrderFields: ['front', 'back', 'lemma', 'translation', 'partOfSpeech', 'inflections'],
  css: `
    table {
      border-collapse: collapse;
      width: 100%;
      max-width: 400px;
      margin-top: 10px;
    }
    th, td { border: 1px solid #aaa; padding: 4px 6px; text-align: center; font-size: 0.9em; }
    th { background-color: #f0f0f0; }
    .lemma { font-size: 1.5em; font-weight: bold; margin-bottom: 10px; }
    .meta { margin-bottom: 10px; font-style: italic; }
    @media (max-width: 480px) { table { font-size: 0.8em; } th, td { padding: 3px; } }
  `,
  isCloze: false,
  cardTemplates: [
    {
      Name: 'Inflection Table Card',
      Front: '<div class="lemma">{{front}}</div>',
      Back: `
        <div class="lemma">{{front}}</div>
        <div class="meta">{{partOfSpeech}}</div>
        <div><strong>Translation:</strong> {{back}}</div>
        <div id="table-container"></div>
        <script>
          const dataField = '{{inflections}}'.trim();
          try {
            const data = JSON.parse(dataField);
            const numbers = Object.keys(data);
            const cases = Object.keys(data[numbers[0]] || {});
            let table = '<table>';
            table += '<tr><th>Case</th>';
            for (let i = 0; i < numbers.length; i++) {
              const num = numbers[i];
              table += '<th>' + num + '</th>';
            }
            table += '</tr>';
            for (let ci = 0; ci < cases.length; ci++) {
              const caseName = cases[ci];
              table += '<tr><th>' + caseName + '</th>';
              for (let ni = 0; ni < numbers.length; ni++) {
                const num = numbers[ni];
                table += '<td>' + (data[num] && data[num][caseName] ? data[num][caseName] : '') + '</td>';
              }
              table += '</tr>';
            }
            table += '</table>';
            const container = document.getElementById('table-container');
            if (container) { container.innerHTML = table; }
          } catch (e) {
            const container = document.getElementById('table-container');
            if (container) { container.innerHTML = '<em>Invalid inflection data</em>'; }
          }
        </script>
        <hr>
        `,
    },
  ],
};
