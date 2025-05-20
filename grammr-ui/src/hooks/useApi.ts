import { useCallback, useState } from 'react';

interface ApiError {
  code: number;
  message: string;
}

export const useApi = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<ApiError | null>(null);

  const request = useCallback(
    async <T>(input: RequestInfo, init?: RequestInit): Promise<T | null> => {
      setIsLoading(true);
      setError(null);

      try {
        const response = await fetch(input, {
          credentials: 'same-origin',
          ...init,
        });

        if (response.status === 401) {
          throw { code: 401, message: 'Unauthorized' };
        }

        const data = await response.json();
        setIsLoading(false);
        return data;
      } catch (err: any) {
        const apiError: ApiError = err?.code ? err : { code: 500, message: 'Something went wrong' };
        setError(apiError);
        setIsLoading(false);
        return null;
      }
    },
    [],
  );

  return { isLoading, error, request };
};
