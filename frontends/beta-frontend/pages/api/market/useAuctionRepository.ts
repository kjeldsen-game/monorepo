import { connectorAPI } from '@/libs/fetcher';
import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';
import useSWR, { mutate } from 'swr';

const API = '/market/auction';

const fetcher = (token: string | null, initialFilter?: string) => {
  if (token === null) return undefined;
  console.log(
    '[useAuctionRepository] Executing fetcher method params ' + initialFilter,
  );
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
  const { setError } = useError();
  const { setNotification } = useNotification();

  const { data: auctions, mutate } = useSWR<any>(
    token ? API + initialFilter : null,
    () => fetcher(token ? token : null, initialFilter),
  );

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
        setNotification('You successfully placed bid for player!');
        mutate();
        return response;
      })
      .catch((error) => {
        setError(error.message);
      });
  };

  return { auctions, updateAuction };
};

export { useAuctionRepository };
