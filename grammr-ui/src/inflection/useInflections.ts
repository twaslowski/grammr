import { useEffect, useState } from 'react';
import { fetchInflections } from './lib';
import {
  Inflections,
  InflectionsNotAvailableError,
} from '@/inflection/types/inflections';

export const useInflections = (
  lemma: string,
  pos: string,
  languageCode: string,
) => {
  const [inflections, setInflections] = useState<Inflections | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [notAvailableInfo, setNotAvailableInfo] = useState<string | null>(null);

  useEffect(() => {
    const loadInflections = async (): Promise<void> => {
      setError(null);
      setInflections(null);
      setNotAvailableInfo(null);

      try {
        const fetchedInflections = await fetchInflections(
          lemma,
          pos,
          languageCode,
        );
        setInflections(fetchedInflections);
      } catch (err) {
        if (err instanceof InflectionsNotAvailableError) {
          setNotAvailableInfo(err.message);
        } else {
          setError('Unknown error when loading inflections');
        }
      }
    };

    void loadInflections();
  }, [lemma, pos, languageCode]);

  return { inflections, error, notAvailableInfo };
};
