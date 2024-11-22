import { connectorAPI } from '@/libs/fetcher';
import useSWR, { mutate } from 'swr';

const API = '/player';

const fetcher = (playerId: string | null | string[], token: string | null) => {
  if (token === null) return undefined;
  return connectorAPI<any>(
    playerId ? `${API}/${playerId}` : API,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const usePlayerRepository = (
  playerId?: string | undefined | string[],
  token?: string,
) => {
  const { data, mutate, error, isLoading } = useSWR<any>(
    token ? `${API}/${playerId}` : null,
    () => fetcher(playerId ? playerId : null, token ? token : null),
  );

  const refetch = () => {
    mutate();
  };

  const sellPlayer = (): Promise<any> => {
    if (!playerId) return Promise.reject('No player ID provided');

    return connectorAPI<any>(
      `/player/${playerId}/sell`,
      'PUT',
      undefined,
      undefined,
      token,
    )
      .then((response) => {
        mutate();
        return response;
      })
      .catch((error) => {
        console.error('Error selling player: ', error);
        throw error;
      });
  };

  return { data, error, isLoading, refetch, sellPlayer };
};

export { usePlayerRepository };
