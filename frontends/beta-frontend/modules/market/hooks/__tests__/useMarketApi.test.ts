import { useMarketApi } from '../useMarketApi';
import * as marketApi from '../../services/marketApi';
import { useSession } from 'next-auth/react';
import { useSnackbar } from 'notistack';
import useSWR from 'swr';
import { act } from 'react';
import { renderHook } from '@testing-library/react';

jest.mock('next-auth/react');
jest.mock('notistack');
jest.mock('swr');
jest.mock('../../services/marketApi');

describe('useMarketApi', () => {
  const mockedUseSession = useSession as jest.Mock;
  const mockedUseSnackbar = useSnackbar as jest.Mock;
  const mockedSWR = useSWR as jest.Mock;

  beforeEach(() => {
    mockedUseSession.mockReturnValue({
      data: { accessToken: 'token-123' },
      status: 'authenticated',
    });

    mockedUseSnackbar.mockReturnValue({
      enqueueSnackbar: jest.fn(),
    });
  });

  it('should return data from useSWR', () => {
    const dummyData = [{ id: 'auction-1', title: 'Auction 1' }];

    mockedSWR.mockReturnValue({
      data: dummyData,
      error: null,
      isLoading: false,
      mutate: jest.fn(),
    });

    const { result } = renderHook(() => useMarketApi());

    expect(result.current.data).toEqual(dummyData);
    expect(result.current.error).toBeNull();
    expect(result.current.isLoading).toBe(false);
  });

  it('should call placeBid and show success snackbar', async () => {
    const enqueueSnackbarMock = jest.fn();
    mockedUseSnackbar.mockReturnValue({ enqueueSnackbar: enqueueSnackbarMock });

    // @ts-ignore
    marketApi.placeBid.mockResolvedValue({ message: 'Bid placed' });

    mockedSWR.mockReturnValue({
      data: [],
      error: null,
      isLoading: false,
      mutate: jest.fn(),
    });

    const { result } = renderHook(() => useMarketApi());

    await act(async () => {
      await result.current.handlePlaceBid('auction-1', { amount: 100 });
    });

    expect(marketApi.placeBid).toHaveBeenCalledWith(
      'auction-1',
      { amount: 100 },
      'token-123'
    );
    expect(enqueueSnackbarMock).toHaveBeenCalledWith('Bid placed', { variant: 'success' });
  });

  it('should show error snackbar when placeBid fails', async () => {
    const enqueueSnackbarMock = jest.fn();
    mockedUseSnackbar.mockReturnValue({ enqueueSnackbar: enqueueSnackbarMock });

    // @ts-ignore
    marketApi.placeBid.mockRejectedValue(new Error('Failed to bid'));

    mockedSWR.mockReturnValue({
      data: [],
      error: null,
      isLoading: false,
      mutate: jest.fn(),
    });

    const { result } = renderHook(() => useMarketApi());

    await act(async () => {
      await result.current.handlePlaceBid('auction-1', { amount: 100 });
    });

    expect(enqueueSnackbarMock).toHaveBeenCalledWith('Failed to bid', { variant: 'error' });
  });
});
