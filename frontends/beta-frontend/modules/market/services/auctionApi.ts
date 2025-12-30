import { connectorAPI } from '@/libs/fetcher';

export const AUCTION_API = '/auction';

export const getAuctions = (
  token: string | null,
  page: number = 0,
  size: number = 10,
  teamId: string
) => {
  if (token === null) return undefined;
  return connectorAPI<any>(
    `${AUCTION_API}/history?teamId=${teamId}&page=${page}&size=${size}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};
