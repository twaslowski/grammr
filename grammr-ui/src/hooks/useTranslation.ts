import { useEffect, useState } from 'react';
import { TokenTranslation } from '@/types';

export function useTranslation(context: string, word: string, languageCode: string, skip = false) {
  const [data, setData] = useState<TokenTranslation | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (skip) return;
    setLoading(true);
    setError(null);
    fetch('/api/v1/translate/word/context', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ phrase: context, word, targetLanguage: languageCode }),
    })
      .then((res) => res.json())
      .then(setData)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, [context, word, languageCode, skip]);

  return { data, loading, error };
}
