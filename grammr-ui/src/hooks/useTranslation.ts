import {useEffect, useState} from 'react';
import {TokenTranslation} from '@/types';
import {useApi} from "@/hooks/useApi";

export function useTranslation(context: string, word: string, targetLanguage: string, skip = false) {
  const [data, setData] = useState<TokenTranslation | null>(null);
  const {request, isLoading, error} = useApi();

  useEffect(() => {
    const fetchTranslation = async () => {
      if (skip) return;
      try {
        const data = await request<TokenTranslation>('/api/v2/translations/word', {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({context, word, targetLanguage}),
        })
        setData(data)
      } catch (err) {
        console.error(error)
      }
    }

    void fetchTranslation()
  }, [context, word, targetLanguage, skip]);

  return {data, isLoading, error};
}
