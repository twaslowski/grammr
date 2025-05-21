import { useCallback, useState } from 'react';

interface ApiError {
  code: number;
  message: string;
}

export const UNEXPECTED_ERROR = { code: 500, message: 'unexpected error' };

export const useApi = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<ApiError | null>(null);

  const request = useCallback(async <T>(input: RequestInfo, init?: RequestInit): Promise<T> => {
    setIsLoading(true);
    setError(null);

    try {
      const response = await fetch(input, {
        credentials: 'same-origin',
        ...init,
      });

      if (!response.ok) {
        throw { code: response.status, message: response.statusText };
      }

      return await response.json();
    } catch (err: any) {
      const apiError: ApiError = err?.code ? err : UNEXPECTED_ERROR;
      setError(apiError);
      throw apiError;
    } finally {
      setIsLoading(false);
    }
  }, []);

  return { isLoading, error, request };
};
