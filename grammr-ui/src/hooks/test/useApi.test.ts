import { act, renderHook } from '@testing-library/react';
import { UNEXPECTED_ERROR, useApi } from '@/hooks/useApi';

describe('useApi hook', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should return data when fetch is successful', async () => {
    const expectedData = { message: 'Success' };
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(expectedData),
      }),
    ) as jest.Mock;

    const { result } = renderHook(() => useApi());

    let data: any;
    await act(async () => {
      data = await result.current.request('/test-endpoint');
    });

    expect(data).toEqual(expectedData);
    expect(result.current.isLoading).toBe(false);
    expect(result.current.error).toBeNull();
  });

  it('should throw error for 401 unauthorized', async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: false,
        status: 401,
        statusText: 'Unauthorized',
      }),
    ) as jest.Mock;

    const { result } = renderHook(() => useApi());

    await act(async () => {
      await expect(result.current.request('/test-endpoint')).rejects.toEqual({
        code: 401,
        message: 'Unauthorized',
      });
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.error).toEqual({ code: 401, message: 'Unauthorized' });
  });

  it('should throw error for network failure', async () => {
    jest.spyOn(global, 'fetch').mockRejectedValueOnce(new Error('Network error'));

    const { result } = renderHook(() => useApi());

    await act(async () => {
      await expect(result.current.request('/error-endpoint')).rejects.toEqual(UNEXPECTED_ERROR);
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.error).toEqual(UNEXPECTED_ERROR);
  });
});
