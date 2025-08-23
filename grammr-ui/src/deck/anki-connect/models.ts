import { MODEL_NAMES } from '@/constant/constants';

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
  if (!inflectionsModelExists(models)) {
    await createModel();
  }
  if (!conjugationModelExists(models)) {
    await createConjugationModel();
  }
  if (!declensionModelExists(models)) {
    await createDeclensionModel();
  }
};

export const createModel = async (): Promise<void> => {
  await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'createModel',
      version: 6,
      params: {
        modelName: MODEL_NAMES.INFLECTION,
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

export const createConjugationModel = async (): Promise<void> => {
  await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'createModel',
      version: 6,
      params: {
        modelName: MODEL_NAMES.CONJUGATION,
        inOrderFields: ['front', 'back', 'lemma', 'translation', 'partOfSpeech', 'inflections'],
        css: 'table {border-collapse: collapse; width: 100%; max-width: 400px; margin-top: 10px;} th, td {border: 1px solid #aaa; padding: 4px 6px; text-align: center; font-size: 0.9em;} th {background-color: #f0f0f0;} .lemma {font-size: 1.5em; font-weight: bold; margin-bottom: 10px;} .meta {margin-bottom: 10px; font-style: italic;} @media (max-width: 480px) { table {font-size: 0.8em;} th, td {padding: 3px;}}',
        isCloze: false,
        cardTemplates: [
          {
            Name: 'Conjugation Table Card',
            Front: '<div class="lemma">{{front}}</div>',
            Back: `<div class="lemma">{{front}}</div><div class="meta">{{partOfSpeech}}</div><div><strong>Translation:</strong> {{back}}</div><div id="table-container"></div><script>
const dataField = \`{{inflections}}\`.trim();
try {
  const inflections = JSON.parse(dataField);
  
  // Create conjugation table for verbs (person x number)
  const conjugationTable = {
    singular: { first: '', second: '', third: '' },
    plural: { first: '', second: '', third: '' }
  };
  
  // Parse inflection data
  inflections.forEach(item => {
    let person = '';
    let number = '';
    
    item.features.forEach(feature => {
      if (feature.type === 'PERSON') {
        // Map FIRST/SECOND/THIRD to first/second/third
        const personValue = feature.value.toLowerCase();
        if (personValue === 'first') person = 'first';
        else if (personValue === 'second') person = 'second';
        else if (personValue === 'third') person = 'third';
      } else if (feature.type === 'NUMBER') {
        // Map SING/PLUR to singular/plural
        const numberValue = feature.value.toLowerCase();
        if (numberValue === 'sing') number = 'singular';
        else if (numberValue === 'plur') number = 'plural';
      }
    });
    
    if (person && number && conjugationTable[number] && conjugationTable[number][person] !== undefined) {
      conjugationTable[number][person] = item.inflected;
    }
  });
  
  // Build table HTML
  let table = '<table>';
  table += '<tr><th></th><th>Singular</th><th>Plural</th></tr>';
  table += \`<tr><th>1st person</th><td>\${conjugationTable.singular.first}</td><td>\${conjugationTable.plural.first}</td></tr>\`;
  table += \`<tr><th>2nd person</th><td>\${conjugationTable.singular.second}</td><td>\${conjugationTable.plural.second}</td></tr>\`;
  table += \`<tr><th>3rd person</th><td>\${conjugationTable.singular.third}</td><td>\${conjugationTable.plural.third}</td></tr>\`;
  table += '</table>';
  
  document.getElementById('table-container').innerHTML = table;
} catch (e) {
  document.getElementById('table-container').innerHTML = '<em>Invalid conjugation data</em>';
}
</script><hr>`,
          },
        ],
      },
    }),
  });
};

export const createDeclensionModel = async (): Promise<void> => {
  await fetch('http://localhost:8765', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'createModel',
      version: 6,
      params: {
        modelName: MODEL_NAMES.DECLENSION,
        inOrderFields: ['front', 'back', 'lemma', 'translation', 'partOfSpeech', 'inflections'],
        css: 'table {border-collapse: collapse; width: 100%; max-width: 400px; margin-top: 10px;} th, td {border: 1px solid #aaa; padding: 4px 6px; text-align: center; font-size: 0.9em;} th {background-color: #f0f0f0;} .lemma {font-size: 1.5em; font-weight: bold; margin-bottom: 10px;} .meta {margin-bottom: 10px; font-style: italic;} @media (max-width: 480px) { table {font-size: 0.8em;} th, td {padding: 3px;}}',
        isCloze: false,
        cardTemplates: [
          {
            Name: 'Declension Table Card',
            Front: '<div class="lemma">{{front}}</div>',
            Back: `<div class="lemma">{{front}}</div><div class="meta">{{partOfSpeech}}</div><div><strong>Translation:</strong> {{back}}</div><div id="table-container"></div><script>
const dataField = \`{{inflections}}\`.trim();
try {
  const inflections = JSON.parse(dataField);
  
  // Group by case and number for declension
  const cases = new Set();
  const numbers = new Set();
  const declensionTable = {};
  
  inflections.forEach(item => {
    let caseName = '';
    let number = '';
    
    item.features.forEach(feature => {
      if (feature.type === 'CASE') {
        caseName = feature.value.toLowerCase();
        cases.add(caseName);
      } else if (feature.type === 'NUMBER') {
        number = feature.value.toLowerCase();
        numbers.add(number);
      }
    });
    
    if (caseName && number) {
      if (!declensionTable[caseName]) {
        declensionTable[caseName] = {};
      }
      declensionTable[caseName][number] = item.inflected;
    }
  });
  
  // Build table HTML
  const sortedNumbers = Array.from(numbers).sort();
  const sortedCases = Array.from(cases);
  
  let table = '<table>';
  table += '<tr><th>Case</th>';
  sortedNumbers.forEach(num => {
    table += \`<th>\${num.charAt(0).toUpperCase() + num.slice(1)}</th>\`;
  });
  table += '</tr>';
  
  sortedCases.forEach(caseName => {
    table += \`<tr><th>\${caseName.charAt(0).toUpperCase() + caseName.slice(1)}</th>\`;
    sortedNumbers.forEach(num => {
      const form = declensionTable[caseName] && declensionTable[caseName][num] ? declensionTable[caseName][num] : '';
      table += \`<td>\${form}</td>\`;
    });
    table += '</tr>';
  });
  table += '</table>';
  
  document.getElementById('table-container').innerHTML = table;
} catch (e) {
  document.getElementById('table-container').innerHTML = '<em>Invalid declension data</em>';
}
</script><hr>`,
          },
        ],
      },
    }),
  });
};

function inflectionsModelExists(models: string[]): boolean {
  return models.includes(MODEL_NAMES.INFLECTION);
}

function conjugationModelExists(models: string[]): boolean {
  return models.includes(MODEL_NAMES.CONJUGATION);
}

function declensionModelExists(models: string[]): boolean {
  return models.includes(MODEL_NAMES.DECLENSION);
}
