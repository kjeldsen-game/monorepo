import useSWR, { mutate } from 'swr';
import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';

import { useSession } from 'next-auth/react';
import { PlaceBidRequest } from '../types/requests';
import * as marketApi from '../services/marketApi';
import { AuctionResponse } from '../types/responses';
import { DefaultResponse } from '@/shared/models/Responses';

export const useMarketApi = (filter: string = '') => {
  const { data: userData } = useSession();
  const { setError } = useError();
  const { setNotification } = useNotification();

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
      setNotification(response.message);
      mutate();
    } catch (err: any) {
      setError(err.message || 'Password update failed');
    }
  };

  return {
    data,
    error,
    isLoading,
    handlePlaceBid,
  };
};
