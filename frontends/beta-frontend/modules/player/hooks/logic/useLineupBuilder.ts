import { useEffect, useState } from 'react';
import { Player } from '@/shared/models/player/Player';
import { Lineup } from 'modules/player/types/Lineup';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { filterPlayersByField } from 'modules/player/utils/PlayerUtils';
import { PlayerLineupStatus } from '@/shared/models/player/PlayerLineupStatus';

export const useLineupBuilder = (players: Player[]) => {
  const [lineup, setLineup] = useState<Lineup>({} as Lineup);
  const [bench, setBench] = useState<Player[] | undefined>();

  const buildTripleSlot = (
    activePlayers: Player[],
    position: PlayerPosition,
  ): [Player | undefined, Player | undefined, Player | undefined] => {
    const players: Player[] = filterPlayersByField(
      activePlayers,
      'position',
      position,
    );
    const playerSlots: [
      Player | undefined,
      Player | undefined,
      Player | undefined,
    ] = [undefined, undefined, undefined];
    switch (players.length) {
      case 0:
        return playerSlots;
      case 1:
        playerSlots[1] = players[0];
        return playerSlots;
      case 2:
        playerSlots[0] = players[0];
        playerSlots[2] = players[1];
        return playerSlots;
      case 3:
        playerSlots[0] = players[0];
        playerSlots[1] = players[1];
        playerSlots[2] = players[2];
        return playerSlots;
    }
  };

  const buildDoubleSlot = (
    activePlayers: Player[],
    position: PlayerPosition,
  ): [Player | undefined, Player | undefined] => {
    const players: Player[] = filterPlayersByField(
      activePlayers,
      'position',
      position,
    );
    const playerSlots: [Player | undefined, Player | undefined] = [
      undefined,
      undefined,
    ];
    switch (players.length) {
      case 0:
        return playerSlots;
      case 1:
        playerSlots[1] = players[0];
        return playerSlots;
      case 2:
        playerSlots[0] = players[0];
        playerSlots[1] = players[1];
        return playerSlots;
    }
  };

  const buildSingleSlot = (
    activePlayers: Player[],
    position: PlayerPosition,
  ): Player | undefined => {
    return (
      filterPlayersByField(activePlayers, 'position', position)[0] || undefined
    );
  };

  useEffect(() => {
    if (!players || players.length === 0) return;

    const benchPlayers = filterPlayersByField(
      players,
      'status',
      PlayerLineupStatus.BENCH,
    );
    const activePlayers = filterPlayersByField(
      players,
      'status',
      PlayerLineupStatus.ACTIVE,
    );

    setLineup({
      GOALKEEPER: buildSingleSlot(activePlayers, PlayerPosition.GOALKEEPER),
      LEFT_BACK: buildSingleSlot(activePlayers, PlayerPosition.LEFT_BACK),
      CENTRE_BACK: buildTripleSlot(activePlayers, PlayerPosition.CENTRE_BACK),
      RIGHT_BACK: buildSingleSlot(activePlayers, PlayerPosition.RIGHT_BACK),
      DEFENSIVE_MIDFIELDER: buildDoubleSlot(
        activePlayers,
        PlayerPosition.DEFENSIVE_MIDFIELDER,
      ),
      OFFENSIVE_MIDFIELDER: buildDoubleSlot(
        activePlayers,
        PlayerPosition.OFFENSIVE_MIDFIELDER,
      ),
      CENTRE_MIDFIELDER: buildTripleSlot(
        activePlayers,
        PlayerPosition.CENTRE_MIDFIELDER,
      ),
      LEFT_MIDFIELDER: buildSingleSlot(
        activePlayers,
        PlayerPosition.LEFT_MIDFIELDER,
      ),
      RIGHT_MIDFIELDER: buildSingleSlot(
        activePlayers,
        PlayerPosition.RIGHT_MIDFIELDER,
      ),
      LEFT_WINGBACK: buildSingleSlot(
        activePlayers,
        PlayerPosition.LEFT_WINGBACK,
      ),
      RIGHT_WINGBACK: buildSingleSlot(
        activePlayers,
        PlayerPosition.RIGHT_WINGBACK,
      ),
      LEFT_WINGER: buildSingleSlot(activePlayers, PlayerPosition.LEFT_WINGER),
      RIGHT_WINGER: buildSingleSlot(activePlayers, PlayerPosition.RIGHT_WINGER),
      FORWARD: buildTripleSlot(activePlayers, PlayerPosition.FORWARD),
    });
    
    setBench(benchPlayers.length > 0 ? benchPlayers : undefined);
  }, [players]);

  return { lineup, bench };
};
