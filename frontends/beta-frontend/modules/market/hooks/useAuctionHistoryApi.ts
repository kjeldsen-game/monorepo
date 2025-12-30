import useSWR from "swr";

import * as auctionApi from '../services/auctionApi';
import { useSession } from "next-auth/react";

export const useAuctionHistoryApi = (size: number, page: number) => {
  const { data: userData } = useSession();

  const { data, error, isLoading, mutate } = useSWR<any>(
    userData?.accessToken ? auctionApi.AUCTION_API + page + size : null,
    () => auctionApi.getAuctions(userData.accessToken!, page, size, userData?.user.teamId!),
  );

  return {
    data,
    error,
    isLoading,
  };
}