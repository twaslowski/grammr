import { Check, X } from 'lucide-react';
import React from 'react';

interface LanguageFeatureSupport {
  [language: string]: {
    [feature: string]: boolean;
  };
}

interface MultiLanguageFeatureTableProps {
  languageFeatures: LanguageFeatureSupport;
}

const DEFAULT_FEATURES = [
  'sentenceTranslation',
  'literalWordTranslation',
  'morphologicalAnalysis',
  'verbConjugation',
  'nounDeclension',
];

// Helper function to convert camelCase to readable text
const formatFeatureName = (feature: string): string => {
  return feature
    .replace(/([A-Z])/g, ' $1') // Insert space before capital letters
    .replace(/^./, (str) => str.toUpperCase()); // Capitalize first letter
};

const MultiLanguageFeatureTable: React.FC<MultiLanguageFeatureTableProps> = ({
  languageFeatures,
}) => {
  // Get all unique languages
  const languages = Object.keys(languageFeatures);

  return (
    <div className='w-full overflow-x-auto bg-white shadow-md rounded-lg'>
      <table className='w-full min-w-[800px]'>
        <thead>
          <tr className='bg-blue-50 border-b'>
            <th className='p-3 text-left font-semibold text-gray-700 sticky left-0 bg-blue-50'>
              Feature / Language
            </th>
            {languages.map((language) => (
              <th
                key={language}
                className='p-3 text-center font-semibold text-gray-700'
              >
                {language}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {DEFAULT_FEATURES.map((feature) => (
            <tr
              key={feature}
              className='border-b hover:bg-gray-50 transition-colors'
            >
              <td className='p-3 text-gray-800 font-medium sticky left-0 bg-white'>
                {formatFeatureName(feature)}
              </td>
              {languages.map((language) => (
                <td key={`${feature}-${language}`} className='p-3 text-center'>
                  {languageFeatures[language][feature] !== undefined ? (
                    languageFeatures[language][feature] ? (
                      <Check className='text-green-500 w-6 h-6 mx-auto' />
                    ) : (
                      <X className='text-red-500 w-6 h-6 mx-auto' />
                    )
                  ) : (
                    <span className='text-gray-400 italic'>N/A</span>
                  )}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MultiLanguageFeatureTable;
