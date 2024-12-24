import {
  Box,
  Button,
  CircularProgress,
  SnackbarCloseReason,
  Tab,
  Tooltip,
  TooltipProps,
  styled,
  tooltipClasses,
} from '@mui/material';
import TeamDetails from './TeamDetails';
import { teamColumn } from '@/shared/components/Grid/Columns/TeamColumn';
import Grid from './Grid/Grid';
import { Player } from '../models/Player';
import { Team } from '../models/Team';
import { useEffect, useMemo, useState } from 'react';
import checkTeamComposition from '../utils/TeamCompositionRules';
import TeamCompositionErrors from './TeamCompositionErrors';
import { CompositionError } from '../models/CompositionError';
import { lineupColumn } from './Grid/Columns/LineupColumn';
import LineupModal from './Team/LineupModal';
import { PlayerOrder } from '@/pages/api/match/models/MatchReportresponse';
import { PlayerPosition } from '../models/PlayerPosition';
import SnackbarAlert from './Common/SnackbarAlert';
import CustomTabs from './CustomTabs';
import { CustomTabPanel } from './Tab/CustomTabPanel';
import MarketButton from './Market/MarketButton';
import Market from '@/pages/market';
import {
  HorizontalPressure,
  Tactic,
  TeamModifiers,
  VerticalPressure,
} from '../models/TeamModifiers';
import TeamModifiersForm from './Team/TeamModifiers';

const CompositionTooltip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} classes={{ popper: className }} />
))(() => ({
  [`& .${tooltipClasses.tooltip}`]: {
    maxWidth: 500,
  },
}));

interface TeamProps {
  isEditing: boolean;
  team: Team | undefined;
  alert?: any;
  setAlert?: any;
  handlePlayerChange?: (value: Player) => void;
  onTeamUpdate?: (players: Player[], teamModifiers: TeamModifiers) => void;
}

const TeamView: React.FC<TeamProps> = ({
  isEditing,
  team,
  alert,
  setAlert,
  handlePlayerChange,
  onTeamUpdate,
}: TeamProps) => {
  const [openModal, setOpenModal] = useState<boolean>(false);

  const [playerEdit, setPlayerEdit] = useState<any>([]);
  const [selectedTab, setSelectedTab] = useState(0);

  const [activePlayers, setActivePlayers] = useState<Player[]>();
  const [benchPlayers, setBenchPlayers] = useState<Player[]>();
  const [players, setPlayers] = useState<Player[]>();
  const [teamModifiers, setTeamModifiers] = useState<TeamModifiers>();
  const [addingStatus, setAddingStatus] = useState<string | undefined>(
    undefined,
  );

  useEffect(() => {
    if (team?.teamModifiers) {
      if (teamModifiers != undefined) {
      } else {
        setTeamModifiers(team?.teamModifiers);
      }
    } else {
      setTeamModifiers(undefined);
    }
  }, [team?.teamModifiers]);

  useEffect(() => {
    console.log(teamModifiers);
  }, [teamModifiers]);

  useEffect(() => {
    if (team?.players) {
      if (activePlayers && activePlayers.length > 0) {
      } else {
        const active = team.players.filter(
          (player) => player.status === 'ACTIVE',
        );
        const bench = team.players.filter(
          (player) => player.status === 'BENCH',
        );
        setActivePlayers(active);
        setBenchPlayers(bench);
        setPlayers(team?.players);
      }
    } else {
      setActivePlayers([]);
      setBenchPlayers([]);
      setPlayers([]);
    }
  }, [team?.players]);

  useEffect(() => {
    if (players && players.length !== 0) {
      const active = players.filter((player) => player.status === 'ACTIVE');
      const bench = players.filter((player) => player.status === 'BENCH');
      setActivePlayers(active);
      setBenchPlayers(bench);
    }
  }, [players]);

  const [compositionErrors, setCompositionErrors] = useState<
    CompositionError[]
  >([]);

  const memoizedCheck = useMemo(
    () => checkTeamComposition(activePlayers ?? []),
    [activePlayers],
  );

  useEffect(() => {
    if (players) {
      setCompositionErrors([...memoizedCheck]);
    }
  }, [activePlayers]);

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
  };

  const memoizedColumns = useMemo(
    () => teamColumn(isEditing, handlePlayerChange),
    [isEditing, handlePlayerChange],
  );

  const memoizedColumns2 = useMemo(
    () => lineupColumn(isEditing, handlePlayerChange),
    [isEditing, handlePlayerChange],
  );

  const handleAddBenchPlayerButton = () => {
    setPlayerEdit(undefined);
    setOpenModal(true);
    setAddingStatus('BENCH');
  };

  const handleAddLineupPlayerButton = () => {
    setPlayerEdit(undefined);
    setOpenModal(true);
    setAddingStatus('ACTIVE');
  };

  const handleAddPlayer = (newPlayer: Player, status: string) => {
    const updatedPlayer = { ...newPlayer, status: status };
    setPlayers((prevPlayers: any) =>
      prevPlayers.map((player: Player) =>
        player.id === newPlayer.id ? { ...player, ...updatedPlayer } : player,
      ),
    );
    setPlayerEdit(status === 'INACTIVE' ? undefined : updatedPlayer);
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
  };

  const handlePlayerFieldChange = (
    player: Player,
    value: PlayerOrder | PlayerPosition,
    field: string,
  ) => {
    const updatedPlayer: Player = { ...player, [field]: value };
    switchPlayerStatuses(updatedPlayer, player);
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
    console.log('handleTeamModifierChange');
    console.log(type);
    console.log(value);
    setTeamModifiers((prevModifiers: any) => ({
      ...prevModifiers,
      [type]: value,
    }));
    console.log(teamModifiers);
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
        <TeamDetails name={team?.name} />
        {/* <PlayerTactics /> */}
        {isEditing && (
          <TeamModifiersForm
            teamModifiers={teamModifiers}
            handleModifierChange={handleTeamModifierChange}
          />
        )}

        {/* <TeamTactics /> */}
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
            <CompositionTooltip
              disableHoverListener={compositionErrors.length === 0}
              placement={'left'}
              title={<TeamCompositionErrors errors={compositionErrors} />}>
              <span>
                <MarketButton
                  sx={{ marginX: '5px' }}
                  onClick={() => {
                    onTeamUpdate(players, teamModifiers);
                    console.log(teamModifiers);
                  }}
                  disabled={compositionErrors.length > 0}>
                  Save
                </MarketButton>
              </span>
            </CompositionTooltip>
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
