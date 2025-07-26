import { connectorAPI } from '@/libs/fetcher';
import { PlaceBidRequest } from '../types/requests';

export const MARKET_API = '/market/auction';

export const getAuctions = (token: string | null, initialFilter?: string) => {
  if (token === null) return undefined;
  return connectorAPI<any>(
    `${MARKET_API}?page=${1}&size=${10}&${initialFilter}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const placeBid = (
  auctionId: string,
  request: PlaceBidRequest,
  token: string,
): Promise<any> => {
  return connectorAPI<any>(
    MARKET_API + '/' + auctionId,
    'PATCH',
    request,
    'include',
    token,
  );
};
