import { InfoIcon, Loader2 } from 'lucide-react';

import { capitalize } from '@/lib/utils';
import { Inflections, InflectionTableData } from '@/inflection/types/inflections';
import React, { useEffect, useState } from 'react';
import { organizeInflectionTable } from '@/inflection/lib';

interface InflectionTableProps {
  textSize?: string;
  showHeader?: boolean;
  inflections: Inflections | null;
  error: string | null;
  notAvailableInfo: string | null;
}

const InflectionTable: React.FC<InflectionTableProps> = ({
  showHeader = true,
  textSize = 'text-md',
  inflections,
  error,
  notAvailableInfo,
}) => {
  const [inflectionTable, setInflectionTable] = useState<InflectionTableData>({});

  useEffect(() => {
    if (inflections) {
      const inflectionTable = organizeInflectionTable(inflections);
      setInflectionTable(inflectionTable);
    }
  }, [inflections]);

  return (
    <>
      {showHeader && <h3 className='text-lg font-semibold'>Inflections</h3>}

      {inflections === null && error === null && notAvailableInfo === null && (
        <div className='flex items-center justify-center py-4' role='status' aria-live='polite'>
          <Loader2 className='h-6 w-6 animate-spin text-gray-500' />
        </div>
      )}

      {error && <div className='text-red-500 text-sm p-3 bg-red-50 rounded py-4'>{error}</div>}

      {notAvailableInfo && (
        <div className='text-sm p-3 bg-blue-50 rounded border-b'>
          <InfoIcon className='h-6 w-6 inline-block text-blue-500 mr-2' />
          {notAvailableInfo}
        </div>
      )}

      {inflections && Object.keys(inflectionTable).length > 0 && (
        <div className='overflow-x-auto'>
          <table className='w-full border-collapse'>
            <thead>
              <tr>
                <th className={`${textSize} border px-4 py-2 bg-gray-50`}>
                  {inflections.partOfSpeech === 'VERB' ? 'Person' : 'Case'}
                </th>
                <th className={`${textSize} border px-4 py-2 bg-gray-50`}>Singular</th>
                <th className={`${textSize} border px-4 py-2 bg-gray-50`}>Plural</th>
              </tr>
            </thead>
            <tbody>
              {Object.entries(inflectionTable).map(([caseValue, numbers]) => (
                <tr key={caseValue}>
                  <td className={`${textSize} border px-4 py-2 font-medium`}>
                    {capitalize(caseValue)}
                  </td>
                  <td className={`${textSize} border px-4 py-2`}>{numbers.singular}</td>
                  <td className={`${textSize} border px-4 py-2`}>{numbers.plural}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
};

export default InflectionTable;
