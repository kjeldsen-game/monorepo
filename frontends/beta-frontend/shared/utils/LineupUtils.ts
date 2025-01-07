import { Player } from '../models/Player';

export const filterPlayersByStatus = (data: Player[], status: string) => {
  return data.filter((player) => player.status === status);
};

export const filterPlayersByTeam = (
  players: Player[],
  teamList: Player[] | undefined,
  status: string,
) => {
  return (
    players
      .filter((player) =>
        teamList?.some((teamPlayer: Player) => teamPlayer.id === player.id),
      )
      .map((player) => {
        const playerOrder = teamList?.find(
          (teamPlayer: Player) => teamPlayer.id === player.id,
        )?.playerOrder;

        return {
          ...player,
          status: status,
          playerOrder: playerOrder,
        };
      }) ?? []
  );
};
