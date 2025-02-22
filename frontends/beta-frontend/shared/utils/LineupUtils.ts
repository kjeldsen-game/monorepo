import { Player } from '../models/Player';

export const filterPlayersByStatus = (
  data: Player[] | undefined,
  status: string,
) => {
  if (data === undefined) return [];
  return data.filter((player) => player.status === status);
};

export const filterPlayersByPosition = (
  data: Player[] = [],
  position: string,
) => {
  return data.filter((player) => player.position === position);
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
        const teamPlayer = teamList?.find(
          (teamPlayer: Player) => teamPlayer.id === player.id,
        );

        const playerOrder = teamPlayer?.playerOrder;
        const playerOrderDestinationPitchArea =
          teamPlayer?.playerOrderDestinationPitchArea;
        const position = teamPlayer?.position;

        return {
          ...player,
          status: status,
          position: position,
          playerOrder: playerOrder,
          playerOrderDestinationPitchArea: playerOrderDestinationPitchArea,
        };
      }) ?? []
  );
};
