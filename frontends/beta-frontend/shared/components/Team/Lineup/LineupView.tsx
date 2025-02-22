import React, { useEffect, useState } from 'react';
import LineupButton from './LineupButton';
import { Box } from '@mui/material';
import { Player } from '@/shared/models/Player';
import { filterPlayersByStatus } from '@/shared/utils/LineupUtils';
import { LineupEditProvider } from '@/shared/contexts/LineupEditContext';

interface LineupViewProps {
  players: any;
  edit: boolean;
  activePlayer: Player;
  handleEdit: (
    newPlayer: Player,
    oldPlayer?: Player,
    position?: string,
    inactive?: boolean,
  ) => void;
}

interface Lineup {
  GOALKEEPER: Player | undefined;
  LEFT_BACK: Player | undefined;
  CENTRE_BACK: Player[] | [];
  RIGHT_BACK: Player | undefined;
  DEFENSIVE_MIDFIELD: Player[] | [];
  OFFENSIVE_MIDFIELD: Player[] | [];
  CENTRE_MIDFIELD: Player[] | [];
  LEFT_MIDFIELD: Player | undefined;
  RIGHT_MIDFIELD: Player | undefined;
  LEFT_WINGBACK: Player | undefined;
  RIGHT_WINGBACK: Player | undefined;
  LEFT_WINGER: Player | undefined;
  RIGHT_WINGER: Player | undefined;
  FORWARD: Player[] | [];
}

const LineupView: React.FC<LineupViewProps> = ({
  players,
  edit,
  activePlayer,
  handleEdit,
}) => {
  const [lineup, setLineup] = useState<Lineup>();
  const [bench, setBench] = useState<{ benchPlayers: Player[] } | undefined>(
    undefined,
  );

  useEffect(() => {
    if (players && players.length > 0) {
      const benchPlayers = filterPlayersByStatus(players, 'BENCH');
      const activePlayers = filterPlayersByStatus(players, 'ACTIVE');

      if (activePlayers) {
        // Setting the GOALKEEPER position
        setLineup((prevLineup) => ({
          ...prevLineup,
          GOALKEEPER:
            activePlayers.find((player) => player.position === 'GOALKEEPER') ||
            undefined,
          LEFT_BACK:
            activePlayers.find((player) => player.position === 'LEFT_BACK') ||
            undefined,
          CENTRE_BACK:
            activePlayers.filter(
              (player) => player.position === 'CENTRE_BACK',
            ) || [],
          RIGHT_BACK:
            activePlayers.find((player) => player.position === 'RIGHT_BACK') ||
            undefined,
          DEFENSIVE_MIDFIELD:
            activePlayers.filter(
              (player) => player.position === 'DEFENSIVE_MIDFIELDER',
            ) || [],
          OFFENSIVE_MIDFIELD:
            activePlayers.filter(
              (player) => player.position === 'OFFENSIVE_MIDFIELDER',
            ) || [],
          CENTRE_MIDFIELD:
            activePlayers.filter(
              (player) => player.position === 'CENTRE_MIDFIELDER',
            ) || [],
          LEFT_MIDFIELD:
            activePlayers.find(
              (player) => player.position === 'LEFT_MIDFIELDER',
            ) || undefined,
          RIGHT_MIDFIELD:
            activePlayers.find(
              (player) => player.position === 'RIGHT_MIDFIELDER',
            ) || undefined,
          LEFT_WINGBACK:
            activePlayers.find(
              (player) => player.position === 'LEFT_WINGBACK',
            ) || undefined,
          RIGHT_WINGBACK:
            activePlayers.find(
              (player) => player.position === 'RIGHT_WINGBACK',
            ) || undefined,
          LEFT_WINGER:
            activePlayers.find((player) => player.position === 'LEFT_WINGER') ||
            undefined,
          RIGHT_WINGER:
            activePlayers.find(
              (player) => player.position === 'RIGHT_WINGER',
            ) || undefined,
          FORWARD:
            activePlayers.filter((player) => player.position === 'FORWARD') ||
            [],
        }));
      }
      // Update bench players
      if (benchPlayers.length > 0) {
        setBench({ benchPlayers });
      } else {
        setBench(undefined); // Optionally clear the bench if no players are found
      }
    }
  }, [players]);

  return (
    <>
      <LineupEditProvider
        handleEdit={handleEdit}
        activePlayer={activePlayer}
        edit={edit}>
        {/* <EditContext.Provider value={edit}> */}
        <Box
          padding={'8px'}
          display={'flex'}
          justifyItems={'center'}
          alignItems={'center'}
          height={250}>
          <LineupButton position={'GOALKEEPER'} player={lineup?.GOALKEEPER} />
          <Box
            height={'100%'}
            display={'flex'}
            flexDirection={'column'}
            alignContent={'space-between'}
            justifyContent={'space-between'}>
            <LineupButton position={'LEFT_BACK'} player={lineup?.LEFT_BACK} />
            <Box>
              <>
                <LineupButton
                  position={'CENTRE_BACK'}
                  player={
                    lineup?.CENTRE_BACK &&
                    (lineup?.CENTRE_BACK.length === 3 ||
                      lineup?.CENTRE_BACK.length === 2)
                      ? lineup.CENTRE_BACK[0]
                      : undefined
                  }
                />
                <LineupButton
                  position={'CENTRE_BACK'}
                  player={
                    lineup?.CENTRE_BACK && lineup.CENTRE_BACK.length === 3
                      ? lineup.CENTRE_BACK[1]
                      : lineup?.CENTRE_BACK?.length === 1
                        ? lineup.CENTRE_BACK[0]
                        : undefined
                  }
                />
                <LineupButton
                  position={'CENTRE_BACK'}
                  player={
                    lineup?.CENTRE_BACK &&
                    (lineup?.CENTRE_BACK.length === 3 ||
                      lineup?.CENTRE_BACK.length === 2)
                      ? lineup.CENTRE_BACK[lineup.CENTRE_BACK.length - 1]
                      : undefined
                  }
                />
              </>
            </Box>
            <LineupButton position={'RIGHT_BACK'} player={lineup?.RIGHT_BACK} />
          </Box>
          <Box
            height={'100%'}
            display={'flex'}
            flexDirection={'column'}
            alignContent={'space-between'}
            justifyContent={'space-between'}>
            <LineupButton
              position={'LEFT_WINGBACK'}
              player={lineup?.LEFT_WINGBACK}
            />
            <Box>
              <LineupButton
                position={'DEFENSIVE_MIDFIELDER'}
                player={lineup?.DEFENSIVE_MIDFIELD[0]}
              />
              <LineupButton
                position={'DEFENSIVE_MIDFIELDER'}
                player={lineup?.DEFENSIVE_MIDFIELD[1]}
              />
            </Box>
            <LineupButton
              position={'RIGHT_WINGBACK'}
              player={lineup?.RIGHT_WINGBACK}
            />
          </Box>

          <Box>
            <LineupButton
              position={'LEFT_MIDFIELDER'}
              player={lineup?.LEFT_MIDFIELD}
            />
            <Box>
              <>
                <LineupButton
                  position={'CENTRE_MIDFIELDER'}
                  player={
                    lineup?.CENTRE_MIDFIELD &&
                    (lineup?.CENTRE_MIDFIELD.length === 3 ||
                      lineup?.CENTRE_MIDFIELD.length === 2)
                      ? lineup.CENTRE_MIDFIELD[0]
                      : undefined
                  }
                />
                <LineupButton
                  position={'CENTRE_MIDFIELDER'}
                  player={
                    lineup?.CENTRE_MIDFIELD &&
                    lineup.CENTRE_MIDFIELD.length === 3
                      ? lineup.CENTRE_MIDFIELD[1]
                      : lineup?.CENTRE_MIDFIELD?.length === 1
                        ? lineup.CENTRE_MIDFIELD[0]
                        : undefined
                  }
                />
                <LineupButton
                  position={'CENTRE_MIDFIELDER'}
                  player={
                    lineup?.CENTRE_MIDFIELD &&
                    (lineup?.CENTRE_MIDFIELD.length === 3 ||
                      lineup?.CENTRE_MIDFIELD.length === 2)
                      ? lineup.CENTRE_MIDFIELD[
                          lineup.CENTRE_MIDFIELD.length - 1
                        ]
                      : undefined
                  }
                />
              </>
            </Box>
            <LineupButton
              position={'RIGHT_MIDFIELDER'}
              player={lineup?.RIGHT_MIDFIELD}
            />
          </Box>

          <Box
            sx={{
              justifyItems: 'center',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}>
            <LineupButton
              position={'LEFT_WINGER'}
              player={lineup?.LEFT_WINGER}
            />
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <Box>
                <LineupButton
                  position={'OFFENSIVE_MIDFIELDER'}
                  player={lineup?.OFFENSIVE_MIDFIELD[0]}
                />
                <LineupButton
                  position={'OFFENSIVE_MIDFIELDER'}
                  player={lineup?.OFFENSIVE_MIDFIELD[1]}
                />
              </Box>
              <Box>
                <LineupButton
                  position={'FORWARD'}
                  player={
                    lineup?.FORWARD &&
                    (lineup?.FORWARD.length === 3 ||
                      lineup?.FORWARD.length === 2)
                      ? lineup.FORWARD[0]
                      : undefined
                  }
                />
                <LineupButton
                  position={'FORWARD'}
                  player={
                    lineup?.FORWARD && lineup.FORWARD.length === 3
                      ? lineup.FORWARD[1]
                      : lineup?.FORWARD?.length === 1
                        ? lineup.FORWARD[0]
                        : undefined
                  }
                />
                <LineupButton
                  position={'FORWARD'}
                  player={
                    lineup?.FORWARD &&
                    (lineup?.FORWARD.length === 3 ||
                      lineup?.FORWARD.length === 2)
                      ? lineup.FORWARD[lineup.FORWARD.length - 1]
                      : undefined
                  }
                />
              </Box>
            </Box>
            <LineupButton
              position={'RIGHT_WINGER'}
              player={lineup?.RIGHT_WINGER}
            />
          </Box>
        </Box>
        {/* Bench Section */}
        <Box
          padding={'8px'}
          borderLeft={'1px #0000004D solid'}
          display={'flex'}
          flexDirection={'column'}
          alignItems={'center'}
          justifyItems={'center'}>
          <Box display={'flex'}>
            <Box>
              <LineupButton player={bench?.benchPlayers?.[0] || undefined} />
              <LineupButton player={bench?.benchPlayers?.[1] || undefined} />
              <LineupButton player={bench?.benchPlayers?.[2] || undefined} />
            </Box>
            <Box>
              <LineupButton player={bench?.benchPlayers?.[3] || undefined} />
              <LineupButton player={bench?.benchPlayers?.[4] || undefined} />
              <LineupButton player={bench?.benchPlayers?.[5] || undefined} />
            </Box>
          </Box>
          <LineupButton player={bench?.benchPlayers?.[6] || undefined} />
        </Box>
      </LineupEditProvider>
    </>
  );
};

export default LineupView;
