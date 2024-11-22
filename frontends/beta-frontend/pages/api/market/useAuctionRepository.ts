import { connectorAPI } from '@/libs/fetcher';
import useSWR, { mutate } from 'swr';

const API = '/market/auction';

const fetcher = (token: string | null, initialFilter?: string) => {
  if (token === null) return undefined;
  return connectorAPI<any>(
    `${API}?page=${1}&size=${10}&${initialFilter}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useAuctionRepository = (
  auction?: string,
  token?: string,
  initialFilter: string = '',
) => {
  const { data: auctions, mutate } = useSWR<any>(
    token ? API + initialFilter : null,
    () => fetcher(token ? token : null, initialFilter),
  );

  const refetch = () => {
    mutate();
  };

  const updateAuction = (value: number): Promise<any> => {
    const newData = {
      amount: value,
    };

    if (!auction) return Promise.reject('No auction ID provided');

    return connectorAPI<any>(
      API + '/' + auction,
      'PATCH',
      newData,
      'include',
      token,
    )
      .then((response) => {
        mutate();
        return response;
      })
      .catch((error) => {
        console.error('Error updating auction:', error);
        throw error;
      });
  };

  return { auctions, updateAuction, refetch };
};

export { useAuctionRepository };
