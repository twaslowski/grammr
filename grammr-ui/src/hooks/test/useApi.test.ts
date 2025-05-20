import { act, renderHook } from '@testing-library/react';
import { useApi } from '@/hooks/useApi';

describe('useApi hook', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should return data when fetch is successful', async () => {
    const expectedData = { message: 'Success' };
    global.fetch = jest.fn(() =>
      Promise.resolve({
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

  it('should set error for 401 unauthorized', async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        status: 401,
      }),
    ) as jest.Mock;

    const { result } = renderHook(() => useApi());

    let data: any;
    await act(async () => {
      data = await result.current.request('/test-endpoint');
    });

    expect(data).toBeNull();
    expect(result.current.isLoading).toBe(false);
    expect(result.current.error).toEqual({ code: 401, message: 'Unauthorized' });
  });

  it('should set error for network failure', async () => {
    jest.spyOn(global, 'fetch').mockRejectedValueOnce(new Error('Network error'));

    const { result } = renderHook(() => useApi());

    let data: any;
    await act(async () => {
      data = await result.current.request('/error-endpoint');
    });

    expect(data).toBeNull();
    expect(result.current.isLoading).toBe(false);
    expect(result.current.error).toEqual({ code: 500, message: 'Something went wrong' });
  });
});
