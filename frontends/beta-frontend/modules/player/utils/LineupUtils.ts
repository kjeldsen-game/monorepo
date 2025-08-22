import { MatchTeam } from '@/shared/models/match/Match';
import { Player } from '@/shared/models/player/Player';
import { POSITION_FILTER_MAP } from '@/shared/models/player/PlayerPosition';
import {
  filterPlayersByStatus,
  filterPlayersByTeam,
} from '@/shared/utils/LineupUtils';

export function getFilteredLineupPlayers(
  players: Player[] = [],
  filter: string,
): Player[] {
  if (filter === 'ALL') {
    return players;
  }

  if (filter === 'ACTIVE' || filter === 'BENCH' || filter === 'INACTIVE') {
    return filterPlayersByStatus(players, filter);
  }

  const allowedPositions = POSITION_FILTER_MAP[filter];
  if (allowedPositions) {
    return players.filter((player) =>
      allowedPositions.includes(player.preferredPosition),
    );
  }

  return players;
}

export const mergePlayers = (
  data,
  matchTeam: MatchTeam,
  setPlayers: (player: Player[]) => void,
) => {
  let lineupPlayers: any[] = [];
  let benchPlayers: any[] = [];
  let inactivePlayers: any[] = [];

  if (!matchTeam?.specificLineup && data?.players) {
    lineupPlayers = filterPlayersByStatus(data?.players, 'ACTIVE');
    benchPlayers = filterPlayersByStatus(data?.players, 'BENCH');
    inactivePlayers = filterPlayersByStatus(data?.players, 'INACTIVE');
  } else {
    if (matchTeam && data?.players) {
      lineupPlayers = filterPlayersByTeam(
        data?.players,
        matchTeam?.players,
        'ACTIVE',
      );
      benchPlayers = filterPlayersByTeam(
        data?.players,
        matchTeam?.bench,
        'BENCH',
      );
      inactivePlayers =
        data?.players
          .filter(
            (player) =>
              !matchTeam?.players.some(
                (lineupPlayer: Player) => lineupPlayer.id === player.id,
              ) &&
              !matchTeam?.bench.some(
                (benchPlayer: Player) => benchPlayer.id === player.id,
              ),
          )
          .map((player) => {
            return {
              ...player,
              position: null,
              status: 'INACTIVE',
            };
          }) ?? [];
    } else {
      lineupPlayers = [];
      benchPlayers = [];
      inactivePlayers = [];
    }
  }
  const allPlayers = [...lineupPlayers, ...benchPlayers, ...inactivePlayers];
  setPlayers(allPlayers);
};
