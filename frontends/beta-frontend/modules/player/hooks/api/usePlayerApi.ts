import { useSession } from 'next-auth/react';
import useSWR, { useSWRConfig } from 'swr';
import * as playerApi from '../../services/playerApi';
import { useSnackbar } from 'notistack';
import { TeamPlayerPatchRequest } from 'modules/player/types/Requests';
import { MARKET_API } from 'modules/market/services/marketApi';

export const usePlayerApi = (playerId?: string) => {
  const { mutate: mutateAuction } = useSWRConfig();
  const { enqueueSnackbar } = useSnackbar();
  const { data: userData } = useSession();

  const { data, error, isLoading, mutate } = useSWR(
    userData?.accessToken
      ? playerApi.PLAYER_API + (playerId ? playerId : userData?.user?.teamId)
      : null,
    () => playerApi.getPlayer(playerId, userData?.accessToken),
  );

  const handleSellPlayerRequest = async (): Promise<void> => {
    try {
      const response = await playerApi.sellPlayer(
        playerId,
        userData?.accessToken,
      );
      enqueueSnackbar('Player was successfully put on market', {
        variant: 'success',
      });
      mutateAuction(MARKET_API + `playerId=${playerId}`);
      mutate();
    } catch (err: any) {
      enqueueSnackbar(err.message, { variant: 'error' });
    }
  };

  return {
    data,
    error,
    isLoading,
    handleSellPlayerRequest,
  };
};
