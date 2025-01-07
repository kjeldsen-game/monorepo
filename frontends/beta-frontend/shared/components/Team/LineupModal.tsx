import CustomModal from '../CustomModal';
import {
  Box,
  CircularProgress,
  IconButton,
  MenuItem,
  Select,
  Typography,
} from '@mui/material';
import Grid from '../Grid/Grid';
import { useEffect, useMemo, useState } from 'react';
import { lineupSelectionColumn } from '../Grid/Columns/LineupSelectionColum';
import { Player } from '@/shared/models/Player';
import { PlayerPosition } from '@/shared/models/PlayerPosition';
import { PlayerOrder } from '@/pages/api/match/models/MatchReportresponse';
import CloseButton from '../Common/CloseButton';
import SelectInput from '../Common/SelectInput';

interface LineupModalProps {
  addingStatus: string | undefined;
  open: boolean;
  player: Player;
  players: Player[];
  handleCloseModal: () => void;
  handleSelectButtonClick: (newPlayer: Player, oldPlayer: Player) => void;
  handlePlayerValueChange: (
    player: Player,
    value: PlayerOrder | PlayerPosition,
    field: string,
  ) => void;
  handlePlayerAdd: (player: Player, status: string) => void;
}

const LineupModal: React.FC<LineupModalProps> = ({
  addingStatus,
  open,
  player,
  players,
  handleCloseModal,
  handleSelectButtonClick,
  handlePlayerValueChange,
  handlePlayerAdd,
}) => {
  const [filterPosition, setFilterPosition] = useState<string>();
  const [filteredPlayers, setFilteredPlayers] = useState<Player[]>([]);
  useEffect(() => {
    const newFilteredPlayers = filterPosition
      ? players.filter((row) => row.position === filterPosition)
      : players;
    setFilteredPlayers(newFilteredPlayers);
  }, [filterPosition, players]);

  const handlePlayerDelete = (newPlayer: Player, oldPlayer = player) => {
    handlePlayerAdd(newPlayer, 'INACTIVE');
  };

  const handleRowButtonClick = (newPlayer: Player, oldPlayer = player) => {
    console.log('click');
    if (player === undefined) {
      console.log('handlePlayerAdd');
      handlePlayerAdd(newPlayer, addingStatus);
    } else {
      console.log('handleSelectButtonClick');
      handleSelectButtonClick(newPlayer, oldPlayer);
    }
  };

  const handleFilterChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    setFilterPosition(event.target.value as string);
  };

  const memoizedColumnsPlayer = useMemo(
    () =>
      lineupSelectionColumn(
        true,
        player,
        handlePlayerDelete,
        handlePlayerValueChange,
      ),
    [player],
  );

  const memoizedColumnsTeamPlayers = useMemo(
    () => lineupSelectionColumn(false, player, handleRowButtonClick),
    [player, addingStatus],
  );

  return (
    <>
      <CustomModal open={open} onClose={handleCloseModal}>
        <Box>
          <CloseButton handleCloseModal={handleCloseModal} />
          {player && (
            <>
              <Grid
                sx={{ marginTop: '20px', minWidth: '1100px' }}
                rows={[player]}
                columns={memoizedColumnsPlayer}
              />
            </>
          )}
          {players ? (
            <>
              <Box
                sx={{
                  background: '#F9F9F9',
                  padding: '10px',
                  borderRadius: '8px',
                  marginY: '5px',
                }}>
                <SelectInput
                  title={'Position'}
                  handleChange={handleFilterChange}
                  value={filterPosition}
                  values={PlayerPosition}
                />
              </Box>
              <Grid
                autoHeight={false}
                sx={{
                  maxHeight: '500px',
                  minHeight: '400px',
                  minWidth: '1100px',
                }}
                rows={filteredPlayers}
                columns={memoizedColumnsTeamPlayers}
              />
            </>
          ) : (
            <>
              <CircularProgress />
            </>
          )}
        </Box>
      </CustomModal>
    </>
  );
};

export default LineupModal;
