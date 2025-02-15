import { Box, SnackbarCloseReason, Tab } from '@mui/material';
import Grid from './Grid/Grid';
import { Player } from '../models/Player';
import { Team, TeamFormationValiation } from '../models/Team';
import { useEffect, useMemo, useState } from 'react';
import { lineupColumn } from './Grid/Columns/LineupColumn';
import LineupModal from './Team/LineupModal';
import { PlayerOrder } from '@/pages/api/match/models/MatchReportresponse';
import {
  PlayerPosition,
  TABLE_PLAYER_POSITION_ORDER_DEFENDERS,
  TABLE_PLAYER_POSITION_ORDER_FORWARDS,
  TABLE_PLAYER_POSITION_ORDER_GOALKEEPERS,
  TABLE_PLAYER_POSITION_ORDER_MIDFIELDERS,
} from '../models/PlayerPosition';
import SnackbarAlert from './Common/SnackbarAlert';
import CustomTabs from './CustomTabs';
import { CustomTabPanel } from './Tab/CustomTabPanel';
import MarketButton from './Market/MarketButton';
import {
  HorizontalPressure,
  Tactic,
  TeamModifiers,
  VerticalPressure,
} from '../models/TeamModifiers';
import VerifiedUserIcon from '@mui/icons-material/VerifiedUser';
import GppMaybeIcon from '@mui/icons-material/GppMaybe';
import TeamViewHeader from './Team/TeamViewHeader';
import { GridRowParams } from '@mui/x-data-grid';
import TeamGrid from './Team/TeamGrid';
import { filterPlayersByStatus } from '../utils/LineupUtils';
import TeamValidationModal from './Team/TeamValidationModal';

interface TeamProps {
  isEditing: boolean;
  team: Team | undefined;
  teamFormationValidation?: TeamFormationValiation;
  alert?: any;
  setAlert?: any;
  onTeamUpdate?: VoidFunction;
}

const TeamView: React.FC<TeamProps> = ({
  isEditing,
  team,
  alert,
  setAlert,
  onTeamUpdate,
  teamFormationValidation,
}: TeamProps) => {
  // console.log(team);
  const [openModal, setOpenModal] = useState<boolean>(false);
  const [openModalValidation, setOpenModalValidation] =
    useState<boolean>(false);
  const [showValidation, setShowValidation] = useState<boolean>(false);
  const [playerEdit, setPlayerEdit] = useState<any>([]);
  const [selectedTab, setSelectedTab] = useState(0);

  const [activePlayers, setActivePlayers] = useState<Player[]>();
  const [benchPlayers, setBenchPlayers] = useState<Player[]>();
  const [players, setPlayers] = useState<Player[]>();
  const [teamModifiers, setTeamModifiers] = useState<TeamModifiers>();
  const [addingStatus, setAddingStatus] = useState<string | undefined>(
    undefined,
  );
  const [activeAddMode, setActiveAddMode] = useState<boolean>(false);

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
      setActivePlayers(filterPlayersByStatus(team.players, 'ACTIVE'));
      setBenchPlayers(filterPlayersByStatus(team.players, 'BENCH'));
      setPlayers(team?.players);
    } else {
      setActivePlayers([]);
      setBenchPlayers([]);
      setPlayers([]);
    }
  }, [team?.players]);

  useEffect(() => {
    if (players) {
      // For the team view of /team/{id} filter have to be done on players array
      setActivePlayers(filterPlayersByStatus(players, 'ACTIVE'));
      setBenchPlayers(filterPlayersByStatus(players, 'BENCH'));
    }
  }, [players]);

  const handleClose = (reason?: SnackbarCloseReason) => {
    if (reason === 'clickaway') {
      return;
    }
    setAlert((prev: any) => ({
      ...prev,
      open: false,
    }));
  };

  const handleModalClose = () => {
    setOpenModal(false);
    setActiveAddMode(false);
    onTeamUpdate(players, teamModifiers);
  };

  const memoizedColumns2 = useMemo(() => lineupColumn(), []);

  const handleAddBenchPlayerButton = () => {
    setPlayerEdit(undefined);
    setOpenModal(true);
    setAddingStatus('BENCH');
    setActiveAddMode(true);
  };

  const handleAddLineupPlayerButton = () => {
    setPlayerEdit(undefined);
    setOpenModal(true);
    setAddingStatus('ACTIVE');
    setActiveAddMode(true);
  };

  const handleAddPlayer = (newPlayer: Player, status: string) => {
    const updatedPlayer = { ...newPlayer, status: status };
    setPlayers((prevPlayers: any) =>
      prevPlayers.map((player: Player) =>
        player.id === newPlayer.id ? { ...player, ...updatedPlayer } : player,
      ),
    );
    if (activeAddMode) {
      setPlayerEdit(undefined);
    } else {
      setPlayerEdit(status === 'INACTIVE' ? undefined : updatedPlayer);
    }
    //onTeamUpdate(players, teamModifiers);
  };

  const switchPlayerStatuses = (newPlayer: Player, oldPlayer: Player) => {
    const newPlayerStatus = newPlayer.status;
    const oldPlayerStatus = oldPlayer.status;

    const updatedPlayer1 = { ...newPlayer, status: oldPlayerStatus };
    const updatedPlayer2 = { ...oldPlayer, status: newPlayerStatus };

    setPlayers((prevPlayers: any) => {
      return prevPlayers.map((player: Player) => {
        if (player.id === newPlayer.id) {
          return { ...player, ...updatedPlayer1 };
        }
        if (player.id === oldPlayer.id) {
          return { ...player, ...updatedPlayer2 };
        }
        return player;
      });
    });
    setPlayerEdit(updatedPlayer1);
    //onTeamUpdate(players, teamModifiers);
  };

  const handlePlayerFieldChange = (
    player: Player,
    value: PlayerOrder | PlayerPosition | any,
    field: string,
  ) => {
    const updatedPlayer: Player = { ...player, [field]: value };
    switchPlayerStatuses(updatedPlayer, player);
    //onTeamUpdate(players, teamModifiers);
  };

  const handlePlayerRowClick = (player: Player) => {
    setPlayerEdit(player);
  };

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue);
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

  const handleRowClick = (status: string) => (params: GridRowParams) => {
    if (isEditing) {
      setOpenModal(true);
      setAddingStatus(status);
      handlePlayerRowClick(params.row);
    }
  };

  return (
    <Box sx={{ width: '100%' }}>
      <LineupModal
        addingStatus={addingStatus}
        player={playerEdit}
        players={players || []}
        open={openModal}
        handleCloseModal={handleModalClose}
        handlePlayerValueChange={handlePlayerFieldChange}
        handleSelectButtonClick={switchPlayerStatuses}
        handlePlayerAdd={handleAddPlayer}
      />
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
          alignItems: 'center',
          background: '#F9F9F9',
          padding: '10px',
          borderRadius: '8px',
        }}>
        <TeamViewHeader
          name={team?.name}
          isEditing={isEditing}
          teamModifiers={teamModifiers}
          handleTeamModifierChange={handleTeamModifierChange}
        />
      </Box>
      <Box
        sx={{ paddingTop: '1rem' }}
        display={'flex'}
        justifyContent={'space-between'}
        alignItems={'center'}>
        <CustomTabs selectedTab={selectedTab} handleChange={handleTabChange}>
          <Tab label="Lineup & Bench" />
          <Tab label="Players" />
        </CustomTabs>
        {selectedTab === 0 && isEditing ? (
          <Box>
            <MarketButton
              sx={{ marginX: '5px' }}
              onClick={handleAddBenchPlayerButton}>
              Add Bench Player
            </MarketButton>
            <MarketButton
              sx={{ marginX: '5px' }}
              onClick={handleAddLineupPlayerButton}>
              Add Lineup Player
            </MarketButton>
            <MarketButton onClick={() => setOpenModalValidation(true)}>
              {teamFormationValidation?.valid ? (
                <VerifiedUserIcon />
              ) : (
                <GppMaybeIcon />
              )}
            </MarketButton>
          </Box>
        ) : (
          <></>
        )}
      </Box>
      <Box sx={{ width: '100%' }}>
        <Box>
          <CustomTabPanel value={selectedTab} index={0}>
            {players && (
              <>
                <TeamGrid
                  rows={activePlayers}
                  onRowClick={handleRowClick('ACTIVE')}
                />
                <TeamGrid
                  rows={benchPlayers}
                  onRowClick={handleRowClick('BENCH')}
                  sx={{ marginTop: '20px' }}
                />
              </>
            )}
          </CustomTabPanel>
          <CustomTabPanel value={selectedTab} index={1}>
            {players && <TeamGrid rows={players} />}
          </CustomTabPanel>
        </Box>
      </Box>
    </Box>
  );
};
export default TeamView;
