import {
  Box,
  Collapse,
  SnackbarCloseReason,
  Tab,
  Typography,
} from '@mui/material';
import TeamDetails from './TeamDetails';
import Grid from './Grid/Grid';
import { Player } from '../models/Player';
import { Team, TeamFormationValiation } from '../models/Team';
import { useEffect, useMemo, useState } from 'react';
import { lineupColumn } from './Grid/Columns/LineupColumn';
import LineupModal from './Team/LineupModal';
import { PlayerOrder } from '@/pages/api/match/models/MatchReportresponse';
import { PlayerPosition } from '../models/PlayerPosition';
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
import TeamModifiersForm from './Team/TeamModifiers';
import DoneIcon from '@mui/icons-material/Done';
import CloseIcon from '@mui/icons-material/Close';
import VerifiedUserIcon from '@mui/icons-material/VerifiedUser';
import GppMaybeIcon from '@mui/icons-material/GppMaybe';

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
      const active = team.players.filter(
        (player) => player.status === 'ACTIVE',
      );
      const bench = team.players.filter((player) => player.status === 'BENCH');
      setActivePlayers(active);
      setBenchPlayers(bench);
      setPlayers(team?.players);
      // if there active players are already set, do nothing
      if (activePlayers && activePlayers.length > 0) {
      } else {
      }
    } else {
      setActivePlayers([]);
      setBenchPlayers([]);
      setPlayers([]);
    }
  }, [team?.players]);

  useEffect(() => {
    if (players) {
      // console.log('Players changed, setting new active and bench players.');
      // For the team view of /team/{id} filter have to be done on players array
      const active = players.filter((player) => player.status === 'ACTIVE');
      const bench = players.filter((player) => player.status === 'BENCH');
      setActivePlayers(active);
      setBenchPlayers(bench);
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
    console.log('running this');
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
    onTeamUpdate(players, teamModifiers);
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
    onTeamUpdate(players, teamModifiers);
  };

  const handlePlayerFieldChange = (
    player: Player,
    value: PlayerOrder | PlayerPosition,
    field: string,
  ) => {
    const updatedPlayer: Player = { ...player, [field]: value };
    switchPlayerStatuses(updatedPlayer, player);
    onTeamUpdate(players, teamModifiers);
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
    setTeamModifiers((prevModifiers: any) => ({
      ...prevModifiers,
      [type]: value,
    }));
    onTeamUpdate(players, teamModifiers);
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
        <Box
          sx={{
            width: '50%',
            display: 'flex',
            alignItems: 'center',
            height: '100%',
            padding: '10px',
          }}>
          <TeamDetails name={team?.name} />
          {/* <PlayerTactics /> */}
          {isEditing && (
            <TeamModifiersForm
              teamModifiers={teamModifiers}
              handleModifierChange={handleTeamModifierChange}
            />
          )}
        </Box>
        <Box
          sx={{
            padding: '10px',
            width: '50%',
            height: 200,
            overflow: 'scroll',
          }}>
          <Collapse in={showValidation} timeout="auto">
            <div
              style={{
                borderRadius: '8px',
                padding: '10px',
                maxHeight: '100%',
                overflowY: 'auto',
                background: 'white',
              }}>
              {teamFormationValidation?.items.map((error, index) => (
                <Box
                  sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    width: '100%',
                  }}
                  key={index}>
                  <Typography>{error.message}</Typography>
                  <Box
                    sx={{
                      color: error.valid ? 'green' : 'red',
                    }}>
                    {error.valid ? <DoneIcon /> : <CloseIcon />}
                  </Box>
                </Box>
              ))}
            </div>
          </Collapse>
        </Box>
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
            <MarketButton onClick={() => setShowValidation(!showValidation)}>
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
                <Grid
                  rows={activePlayers || []}
                  columns={memoizedColumns2}
                  onRowClick={(params) => {
                    if (isEditing) {
                      setOpenModal(true);
                      setAddingStatus('ACTIVE');
                      handlePlayerRowClick(params.row);
                    }
                  }}
                />
                <Grid
                  sx={{ marginTop: '20px' }}
                  rows={benchPlayers || []}
                  columns={memoizedColumns2}
                  onRowClick={(params) => {
                    if (isEditing) {
                      setOpenModal(true);
                      setAddingStatus('BENCH');
                      handlePlayerRowClick(params.row);
                    }
                  }}
                />
              </>
            )}
          </CustomTabPanel>
          <CustomTabPanel value={selectedTab} index={1}>
            {players && (
              <Grid rows={players || []} columns={memoizedColumns2} />
            )}
          </CustomTabPanel>
        </Box>
      </Box>
    </Box>
  );
};
export default TeamView;
