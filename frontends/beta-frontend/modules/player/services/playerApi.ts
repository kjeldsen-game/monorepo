import { connectorAPI } from '@/libs/fetcher';
import { Player } from '@/shared/models/player/Player';

export const PLAYER_API = '/player';

export const getPlayer = (
  playerId: string | null,
  token: string | null,
): Promise<Player> => {
  if (token === undefined || playerId === undefined) {
    return undefined;
  }
  return connectorAPI<any>(
    `${PLAYER_API}/${playerId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const sellPlayer = (
  playerId: string | null,
  token: string | null,
): Promise<Player> => {
  if (token === undefined || playerId === undefined) {
    return undefined;
  }
  return connectorAPI<undefined>(
    `${PLAYER_API}/${playerId}/sell`,
    'PUT',
    undefined,
    undefined,
    token,
  );
};
