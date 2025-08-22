import { Player } from '@/shared/models/player/Player';
import {
  HorizontalPressure,
  Tactic,
  TeamModifiers,
  VerticalPressure,
} from '@/shared/models/player/TeamModifiers';
import { useEffect, useState } from 'react';
import { useTeamApi } from '../api/useTeamApi';

export const useLineupManager = (
  players2: Player[],
  teamModifiers2: TeamModifiers,
  handleUpdateLineup: (request: any) => void,
) => {
  const [players, setPlayers] = useState<Player[]>();
  const [teamModifiers, setTeamModifiers] =
    useState<TeamModifiers>(teamModifiers2);
  const [activePlayer, setActivePlayer] = useState<Player | undefined>();
  const { handleLineupUpdateRequest } = useTeamApi();

  const updateFn = handleUpdateLineup ?? handleLineupUpdateRequest;

  useEffect(() => {
    setPlayers(players2);
  }, [players2]);

  useEffect(() => {
    setTeamModifiers(teamModifiers2);
  }, [teamModifiers2]);

  const buildPlayer = (player, status, position) => {
    return {
      ...player,
      status: status,
      position: position,
    };
  };

  const handleTeamModifierChange = (
    value: Tactic | VerticalPressure | HorizontalPressure,
    type: string,
  ) => {
    setTeamModifiers((prevModifiers: any) => {
      const updatedModifiers = { ...prevModifiers, [type]: value };
      updateFn({
        players: players,
        teamModifiers: updatedModifiers,
      });
      return updatedModifiers;
    });
  };

  const handleEdit = (
    newPlayer: Player,
    oldPlayer?: Player,
    position?: string,
    inactive?: boolean,
  ) => {
    if (activePlayer === undefined && !inactive && !newPlayer) {
      if (oldPlayer === undefined) {
        return;
      }
      setActivePlayer(oldPlayer);
      return;
    }
    if (!oldPlayer) {
      // Updating player to the BENCH
      if (!position) {
        console.log('here');
        const updatedPlayer = buildPlayer(
          newPlayer,
          inactive ? 'INACTIVE' : 'BENCH',
          null,
        );
        setPlayers((prevPlayers: any) => {
          const updatedPlayers = prevPlayers.map((player: Player) =>
            player.id === newPlayer.id
              ? { ...player, ...updatedPlayer }
              : player,
          );
          updateFn({
            players: updatedPlayers,
            teamModifiers: teamModifiers,
          });
          return updatedPlayers;
        });
      } else {
        const updatedPlayer = buildPlayer(newPlayer, 'ACTIVE', position);
        setPlayers((prevPlayers: any) => {
          const updatedPlayers = prevPlayers.map((player: Player) =>
            player.id === newPlayer.id
              ? { ...player, ...updatedPlayer }
              : player,
          );
          updateFn({
            players: updatedPlayers,
            teamModifiers: teamModifiers,
          });
          return updatedPlayers;
        });
      }
    } else {
      const updatedPlayer1 = buildPlayer(
        newPlayer,
        oldPlayer.status,
        oldPlayer.position,
      );
      const updatedPlayer2 = buildPlayer(
        oldPlayer,
        newPlayer.status,
        newPlayer.position,
      );

      setPlayers((prevPlayers: any) => {
        const updatedPlayers = prevPlayers.map((player: Player) => {
          if (player.id === newPlayer.id) {
            return { ...player, ...updatedPlayer1 };
          }
          if (player.id === oldPlayer.id) {
            return { ...player, ...updatedPlayer2 };
          }
          return player;
        });
        updateFn({
          players: updatedPlayers,
          teamModifiers: teamModifiers,
        });
        return updatedPlayers;
      });
    }
    setActivePlayer(undefined);
  };

  return {
    handleEdit,
    handleTeamModifierChange,
    players,
    teamModifiers,
    setActivePlayer,
    activePlayer,
  };
};
