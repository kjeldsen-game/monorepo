import useSWR from 'swr';
import { useSession } from 'next-auth/react';
import { PlaceBidRequest } from '../types/requests';
import * as marketApi from '../services/marketApi';
import { AuctionResponse } from '../types/responses';
import { DefaultResponse } from '@/shared/models/Responses';
import { useSnackbar } from 'notistack';

export const useMarketApi = (filter: string = '') => {
  const { data: userData } = useSession();
  const { enqueueSnackbar } = useSnackbar();

  const { data, error, isLoading, mutate } = useSWR<AuctionResponse[]>(
    userData?.accessToken ? marketApi.MARKET_API + filter : null,
    () => marketApi.getAuctions(userData.accessToken!, filter),
  );

  const handlePlaceBid = async (
    auctionId: string,
    request: PlaceBidRequest,
  ): Promise<void> => {
    try {
      const response: DefaultResponse = await marketApi.placeBid(
        auctionId,
        request,
        userData.accessToken!,
      );
      enqueueSnackbar(response.message, { variant: 'success' });
      mutate();
    } catch (err: any) {
      enqueueSnackbar(err.message || 'Password update failed', {
        variant: 'error',
      });
    }
  };

  return {
    data,
    error,
    isLoading,
    handlePlaceBid,
  };
};
