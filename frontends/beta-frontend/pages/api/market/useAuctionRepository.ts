import { connectorAPI } from '@/libs/fetcher';
import useSWR from 'swr';
import { AuctionDetail } from '@/shared/models/AuctionDetail';

const API = '/market/auction/';

const useAuctionRepository = (auction?: string) => {
  const { data, mutate } = useSWR<AuctionDetail>(
    auction ? API + auction : null,
    connectorAPI,
  );

  const updateAuction = (value: number): Promise<any> => {
    if (!data) return Promise.reject('No data available');

    const newData = {
      amount: value,
    };

    if (!auction) return Promise.reject('No auction ID provided');

    return connectorAPI<any>(API + auction, 'PATCH', newData, 'include')
      .then((response) => {
        mutate();
        return response;
      })
      .catch((error) => {
        console.error('Error updating auction:', error);
        throw error;
      });
  };

  return { updateAuction };
};

export { useAuctionRepository };
