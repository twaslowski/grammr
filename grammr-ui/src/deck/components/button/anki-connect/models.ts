export const precheckModels = async () => {
  const response = await fetch('ANKI_CONNECT_URL', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'modelNames',
      version: ANKI_CONNECT_VERSION,
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
  if (!inflectionsModelExists(models)) {
  }
};

export const createModel = async (): Promise<void> => {
  await fetch(ANKI_CONNECT_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'createModel',
      version: ANKI_CONNECT_VERSION,
      params: {
        modelName: INFLECTIONS_MODEL_NAME,
        inOrderFields: ['Lemma', 'Translation', 'PartOfSpeech', 'InflectionData', 'Notes'],
        css: 'table {border-collapse: collapse; width: 100%; max-width: 400px; margin-top: 10px;} th, td {border: 1px solid #aaa; padding: 4px 6px; text-align: center; font-size: 0.9em;} th {background-color: #f0f0f0;} .lemma {font-size: 1.5em; font-weight: bold; margin-bottom: 10px;} .meta {margin-bottom: 10px; font-style: italic;} @media (max-width: 480px) { table {font-size: 0.8em;} th, td {padding: 3px;}}',
        isCloze: false,
        cardTemplates: [
          {
            Name: 'Inflection Table Card',
            Front: '<div class="lemma">{{Lemma}}</div>',
            Back: "<div class=\"lemma\">{{Lemma}}</div><div class=\"meta\">{{PartOfSpeech}}</div><div><strong>Translation:</strong> {{Translation}}</div><div id=\"table-container\"></div><script>const dataField = `{{InflectionData}}`.trim();try {const data = JSON.parse(dataField);const numbers = Object.keys(data);const cases = Object.keys(data[numbers[0]]);let table = '<table>';table += '<tr><th>Case</th>';for (let num of numbers) {table += `<th>${num}</th>`;}table += '</tr>';for (let caseName of cases) {table += `<tr><th>${caseName}</th>`;for (let num of numbers) {table += `<td>${data[num][caseName]}</td>`;}table += '</tr>'; }table += '</table>';document.getElementById('table-container').innerHTML = table;} catch (e) {document.getElementById('table-container').innerHTML = '<em>Invalid inflection data</em>';}</script><hr>{{Notes}}",
          },
        ],
      },
    }),
  });
};

function inflectionsModelExists(models: string[]): boolean {
  return models.includes('inflections');
}
