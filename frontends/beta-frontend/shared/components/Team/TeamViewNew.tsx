import { Player } from '@/shared/models/Player';
import { Team, TeamFormationValiation } from '@/shared/models/Team';
import {
  HorizontalPressure,
  Tactic,
  TeamModifiers,
  VerticalPressure,
} from '@/shared/models/TeamModifiers';
import { filterPlayersByStatus } from '@/shared/utils/LineupUtils';
import { Box, Button, SnackbarCloseReason, Tab } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import { lineupColumn } from '../Grid/Columns/LineupColumn';
import { PlayerOrder } from '@/pages/api/match/models/MatchReportresponse';
import {
  PlayerPosition,
  POSITION_FILTER_MAP,
} from '@/shared/models/PlayerPosition';
import { GridRowParams } from '@mui/x-data-grid';
import TeamValidationModal from './TeamValidationModal';
import SnackbarAlert from '../Common/SnackbarAlert';
import TeamViewHeader from './TeamViewHeader';
import LineupView from './Lineup/LineupView';
import TeamGrid from './TeamGrid';
import MarketButton from '../Market/MarketButton';
import VerifiedUserIcon from '@mui/icons-material/VerifiedUser';
import GppMaybeIcon from '@mui/icons-material/GppMaybe';
import EditIcon from '@mui/icons-material/Edit';
import CloseIcon from '@mui/icons-material/Close';
import { Close, Edit } from '@mui/icons-material';
import LineupButton from './Lineup/LineupButton';
import LineupFilterButton from './Filters/LineupFilterButton';
import CustomTabs from '../CustomTabs';
import { CustomTabPanel } from '../Tab/CustomTabPanel';
import { flexCenter } from '@/shared/styles/common';
import TeamDetails from '../TeamDetails';

interface TeamProps {
  isEditing: boolean;
  team: Team | undefined;
  teamFormationValidation?: TeamFormationValiation;
  alert?: any;
  setAlert?: any;
  onTeamUpdate?: VoidFunction;
}

const TeamViewNew: React.FC<TeamProps> = ({
  isEditing,
  team,
  alert,
  setAlert,
  onTeamUpdate,
  teamFormationValidation,
}: TeamProps) => {
  // New states
  const [edit, setEdit] = useState<boolean>(false);
  const [filter, setFilter] = useState<string>('ALL');
  const [activePlayerV2, setActivePlayerV2] = useState<any>();
  const [selectedTab, setSelectedTab] = useState(0);

  // Old states
  const [openModalValidation, setOpenModalValidation] =
    useState<boolean>(false);
  const [players, setPlayers] = useState<Player[]>();
  const [teamModifiers, setTeamModifiers] = useState<TeamModifiers>();
  // NEW

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue);
  };

  const handleEdit = (
    newPlayer: Player,
    oldPlayer?: Player,
    position?: string,
    inactive?: boolean,
  ) => {
    if (!oldPlayer) {
      // Updating player to the BENCH
      if (!position) {
        console.log('here');
        const updatedPlayer = {
          ...newPlayer,
          status: inactive ? 'INACTIVE' : 'BENCH',
          position: null,
        };
        setPlayers((prevPlayers: any) => {
          const updatedPlayers = prevPlayers.map((player: Player) =>
            player.id === newPlayer.id
              ? { ...player, ...updatedPlayer }
              : player,
          );
          // Call onTeamUpdate immediately with the updated players state
          onTeamUpdate(updatedPlayers, teamModifiers);
          return updatedPlayers;
        });
      } else {
        const updatedPlayer = {
          ...newPlayer,
          status: 'ACTIVE',
          position: position,
        };
        setPlayers((prevPlayers: any) => {
          const updatedPlayers = prevPlayers.map((player: Player) =>
            player.id === newPlayer.id
              ? { ...player, ...updatedPlayer }
              : player,
          );
          onTeamUpdate(updatedPlayers, teamModifiers);
          return updatedPlayers;
        });
      }
    } else {
      const newPlayerStatus = newPlayer.status;
      const oldPlayerStatus = oldPlayer.status;

      const updatedPlayer1 = {
        ...newPlayer,
        status: oldPlayerStatus,
        position: oldPlayer.position,
      };
      const updatedPlayer2 = {
        ...oldPlayer,
        status: newPlayerStatus,
        position: newPlayer.position,
      };

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
        onTeamUpdate(updatedPlayers, teamModifiers);
        return updatedPlayers;
      });
    }
    setActivePlayerV2(undefined); // Reset active player
  };

  const handleSelectChange = (
    player: Player,
    value: PlayerOrder | any,
    field: string,
  ) => {
    console.log(value);
    console.log(field);
    const updatedPlayer: Player = { ...player, [field]: value };
    handleEdit(updatedPlayer, player);
  };

  useEffect(() => {
    if (team?.teamModifiers) {
      setTeamModifiers(team?.teamModifiers);
    } else {
      setTeamModifiers(undefined);
    }
  }, [team?.teamModifiers]);

  useEffect(() => {
    // If players from the props are present
    if (team?.players) {
      setPlayers(team?.players);
    } else {
      setPlayers([]);
    }
  }, [team?.players]);

  const handleClose = (reason?: SnackbarCloseReason) => {
    if (reason === 'clickaway') {
      return;
    }
    setAlert((prev: any) => ({
      ...prev,
      open: false,
    }));
  };

  const handleTeamModifierChange = (
    value: Tactic | VerticalPressure | HorizontalPressure,
    type: string,
  ) => {
    setTeamModifiers((prevModifiers: any) => {
      const updatedModifiers = { ...prevModifiers, [type]: value };
      onTeamUpdate(players, updatedModifiers);
      return updatedModifiers;
    });
  };

  return (
    <Box sx={{ width: '100%' }}>
      <TeamValidationModal
        teamFormationValidation={teamFormationValidation}
        open={openModalValidation}
        handleCloseModal={() => setOpenModalValidation(false)}
      />
      <SnackbarAlert
        handleClose={handleClose}
        open={alert?.open}
        type={alert?.type}
        message={alert?.message}
      />
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          background: '#F9F9F9',
          padding: '10px',
          borderRadius: '8px',
        }}>
        <CustomTabs selectedTab={selectedTab} handleChange={handleTabChange}>
          <Tab label="Lineup" />
          <Tab label="Tactics" />
          {/* <Tab label="Player Orders" /> */}
        </CustomTabs>
        <Box sx={{ ...flexCenter, width: '100%' }}>
          <TeamDetails name={team?.name} />
          <CustomTabPanel value={selectedTab} index={1}>
            <Box sx={{ ...flexCenter }}>
              <TeamViewHeader
                name={team?.name}
                isEditing={isEditing}
                teamModifiers={teamModifiers}
                handleTeamModifierChange={handleTeamModifierChange}
              />
            </Box>
          </CustomTabPanel>
          <CustomTabPanel value={selectedTab} index={0}>
            <Box sx={{ ...flexCenter }}>
              <LineupView
                activePlayer={activePlayerV2}
                players={players}
                edit={edit}
                handleEdit={handleEdit}
              />
            </Box>
          </CustomTabPanel>
        </Box>
      </Box>
      <Box>
        <Box
          display={'flex'}
          alignContent={'center'}
          alignItems={'center'}
          justifyContent={'space-between'}
          sx={{ paddingY: 3 }}>
          <Box>
            <LineupFilterButton name={'ALL'} handleClick={setFilter} />
            <LineupFilterButton name={'GK'} handleClick={setFilter} />
            <LineupFilterButton name={'DEF'} handleClick={setFilter} />
            <LineupFilterButton name={'MID'} handleClick={setFilter} />
            <LineupFilterButton name={'FW'} handleClick={setFilter} />
            <LineupFilterButton name={'ACTIVE'} handleClick={setFilter} />
            <LineupFilterButton name={'BENCH'} handleClick={setFilter} />
          </Box>
          <Box>
            <MarketButton
              sx={{ marginX: '5px' }}
              onClick={() => setEdit(!edit)}>
              {edit ? <CloseIcon /> : <EditIcon />}
            </MarketButton>
            <MarketButton onClick={() => setOpenModalValidation(true)}>
              {teamFormationValidation?.valid ? (
                <VerifiedUserIcon />
              ) : (
                <GppMaybeIcon />
              )}
            </MarketButton>
          </Box>
        </Box>
      </Box>
      <Box sx={{ width: '100%' }}>
        <TeamGrid
          onSelectChange={handleSelectChange}
          isEditing={edit}
          activePlayer={activePlayerV2}
          onButtonClick={setActivePlayerV2}
          rows={
            filter === 'ALL'
              ? players ?? []
              : filter === 'ACTIVE' || filter === 'BENCH'
                ? filterPlayersByStatus(players, filter)
                : players?.filter((player) =>
                    POSITION_FILTER_MAP[filter]?.includes(
                      player.preferredPosition,
                    ),
                  ) ?? []
          }
        />
      </Box>
    </Box>
  );
};
export default TeamViewNew;
