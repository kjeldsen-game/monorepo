import { connectorAPI } from '@/libs/fetcher';
import { Auction, AuctionMarket } from '@/shared/models/Auction';
import { useEffect, useState } from 'react';

const API = '/market/auction';

const useAllAuctionRepository = (
  token?: string,
  initialPage: number = 1,
  initialSize: number = 10,
  initialFilter: string = '',
) => {
  const [data, setData] = useState<AuctionMarket[]>([]);
  const fetchInitialData = async () => {
    const initialData = await connectorAPI(
      `${API}?page=${initialPage}&size=${initialSize}&${initialFilter}`,
      'GET',
      undefined,
      'include',
      token,
    );
    setData(initialData);
  };

  useEffect(() => {
    fetchInitialData();
  }, []);

  const refetch = async (
    page: number = initialPage,
    size: number = initialSize,
    filter: string = initialFilter,
  ) => {
    const newData = await connectorAPI(
      `${API}?page=${page}&size=${size}&${filter}`,
      'GET',
      undefined,
      'include',
    );
    setData(newData);
  };

  return { allAuctions: data, refetch };
};

export { useAllAuctionRepository };
