import { InfoIcon, Loader2 } from 'lucide-react';
import React, { useEffect, useState } from 'react';

import { capitalize } from '@/lib/utils';
import {
  fetchInflections,
  organizeInflectionTable,
} from '@/service/inflection';
import {
  InflectionsNotAvailableError,
  InflectionTableData,
} from '@/types/inflections';
import TokenType from '@/types/tokenType';

interface InflectionTableProps {
  token: TokenType;
  pos: string;
  languageCode: string;
}

const InflectionTable: React.FC<InflectionTableProps> = ({
  token,
  pos,
  languageCode,
}) => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string>('');
  const [inflectionTable, setInflectionTable] = useState<InflectionTableData>(
    {},
  );
  const [inflectionNotAvailableInfo, setInflectionNotAvailableInfo] =
    useState<string>('');

  useEffect(() => {
    const loadInflections = async () => {
      setIsLoading(true);
      try {
        const response = await fetchInflections({
          token,
          languageCode,
        });
        const inflectionTable = organizeInflectionTable(response);
        setInflectionTable(inflectionTable);
      } catch (err) {
        if (err instanceof InflectionsNotAvailableError)
          setInflectionNotAvailableInfo(err.message);
        else if (err instanceof Error) setError(err.message);
        else setError('Unknown error when loading inflections');
      } finally {
        setIsLoading(false);
      }
    };
    loadInflections();
  }, [token, languageCode]);

  return (
    <div>
      <h3 className='text-lg font-semibold'>Inflections</h3>

      {isLoading && (
        <div className='flex items-center justify-center py-4'>
          <Loader2 className='h-6 w-6 animate-spin text-gray-500' />
        </div>
      )}

      {error && (
        <div className='text-red-500 text-sm p-3 bg-red-50 rounded py-4'>
          {error}
        </div>
      )}

      {inflectionNotAvailableInfo && (
        <div className='text-sm p-3 bg-blue-50 rounded border-b'>
          <InfoIcon className='h-6 w-6 inline-block text-blue-500 mr-2' />
          {inflectionNotAvailableInfo}
        </div>
      )}

      {!isLoading &&
        !error &&
        !inflectionNotAvailableInfo &&
        Object.keys(inflectionTable).length > 0 && (
          <div className='overflow-x-auto'>
            <table className='w-full border-collapse'>
              <thead>
                <tr>
                  <th className='border px-4 py-2 bg-gray-50'>
                    {pos === 'VERB' ? 'Person' : 'Case'}
                  </th>
                  <th className='border px-4 py-2 bg-gray-50'>Singular</th>
                  <th className='border px-4 py-2 bg-gray-50'>Plural</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(inflectionTable).map(([caseValue, numbers]) => (
                  <tr key={caseValue}>
                    <td className='border px-4 py-2 font-medium'>
                      {capitalize(caseValue)}
                    </td>
                    <td className='border px-4 py-2'>{numbers.singular}</td>
                    <td className='border px-4 py-2'>{numbers.plural}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
    </div>
  );
};

export default InflectionTable;
