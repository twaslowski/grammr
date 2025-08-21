import { useEffect, useState } from 'react';
import { Paradigm } from '@/flashcard/types/paradigm';
import { useApi } from '@/hooks/useApi';

export const useInflections = (lemma: string, pos: string, languageCode: string) => {
  const [inflections, setInflections] = useState<Paradigm | null>(null);
  const { isLoading, request, error } = useApi();

  useEffect(() => {
    const fetchInflections = async (
      lemma: string,
      pos: string,
      languageCode: string,
    ): Promise<Paradigm> => {
      return await request<Paradigm>(`/api/v1/inflection`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          lemma: lemma,
          partOfSpeechTag: pos,
          languageCode: languageCode,
        }),
      });
    };

    fetchInflections(lemma, pos, languageCode).then((data) => {
      setInflections(data);
    });
  }, [lemma, pos, languageCode, request]);

  const fetchExisting = async (paradigmId: string) => {
    return await request<Paradigm>(`/api/v1/inflection/${paradigmId}`, {
      method: 'GET',
    })
      .then((data) => {
        setInflections(data);
      })
      .catch((err) => {
        console.error('Error fetching existing inflection:', err);
        throw err;
      });
  };

  return { inflections, error, isLoading, fetchExisting };
};
