import { Box, CircularProgress } from '@mui/material';
import React, { useEffect, useMemo } from 'react';
import CustomModal from '../CustomModal';
import CloseButton from '../Common/CloseButton';
import { matchStatsColumns } from '../Grid/Columns/MatchReport/MatchStatsColumns';
import Grid from '../Grid/Grid';
import SelectInput from '../Common/SelectInput';
import { PITCH_AREAS_ENUM, PitchArea } from '@/shared/models/match/PitchArea';
import { Action } from '@/shared/models/match/Play';
import { BallHeight } from '@/shared/models/match/BallState';

interface MatchStatsModalProps {
  players: any;
  data: any;
  open: boolean;
  handleCloseModal: () => void;
}

const MatchStatsModal: React.FC<MatchStatsModalProps> = ({
  players,
  data,
  open,
  handleCloseModal,
}) => {
  //   console.log(data);

  const [filteredData, setFilteredData] = React.useState<any[]>([]);
  const columns = useMemo(() => matchStatsColumns(), [data]);
  const [filters, setFilter] = React.useState({
    action: '',
    ballHeight: '',
    pitchArea: '',
    initiator: '',
    challenger: '',
  });

  useEffect(() => {
    if (!data) return;
    console.log(filters);

    const filtered = data.filter((row: any) => {
      return (
        (!filters.initiator ||
          row.duel.initiator.name +
            ` [${row.duel.initiator.teamRole.substring(0, 1)}]` ===
            filters.initiator) &&
        (!filters.challenger ||
          row.duel.challenger?.name +
            ` [${row.duel.challenger?.teamRole.substring(0, 1)}]` ===
            filters.challenger) &&
        (!filters.action || row.action === filters.action) &&
        (!filters.ballHeight || row.ballState.height === filters.ballHeight) &&
        (!filters.pitchArea || row.duel.pitchArea === filters.pitchArea)
      );
    });

    setFilteredData(filtered);
  }, [data, filters]);

  const handleFilterChange = (
    value: Action | PitchArea | BallHeight,
    filter: string,
  ) => {
    setFilter((prev) => ({ ...prev, [filter]: value }));
  };

  return (
    <CustomModal open={open} onClose={handleCloseModal}>
      <CloseButton handleCloseModal={handleCloseModal} />
      <Box sx={{ width: '1400px' }}>
        <Box paddingY={'10px'} display={'flex'} justifyContent={'space-evenly'}>
          <Box>
            <SelectInput
              handleChange={(e) => handleFilterChange(e.target.value, 'action')}
              value={filters.action}
              values={Action}
              hideDefaultOption={false}
              title={'Action'}
            />
          </Box>
          <Box>
            <SelectInput
              handleChange={(e) =>
                handleFilterChange(e.target.value, 'ballHeight')
              }
              value={filters.ballHeight}
              values={BallHeight}
              hideDefaultOption={false}
              title={'Ball Height'}
            />
          </Box>
          <Box>
            <SelectInput
              handleChange={(e) =>
                handleFilterChange(e.target.value, 'pitchArea')
              }
              value={filters.pitchArea}
              values={PITCH_AREAS_ENUM}
              hideDefaultOption={false}
              title={'Pitch Area'}
            />
          </Box>
          <Box>
            <SelectInput
              handleChange={(e) =>
                handleFilterChange(e.target.value, 'initiator')
              }
              value={filters.initiator}
              values={players}
              hideDefaultOption={false}
              title={'Initiator'}
            />
          </Box>
          <Box>
            <SelectInput
              handleChange={(e) =>
                handleFilterChange(e.target.value, 'challenger')
              }
              value={filters.challenger}
              values={players}
              hideDefaultOption={false}
              title={'Challenger'}
            />
          </Box>
        </Box>
        {data ? (
          <Grid
            sx={{
              maxHeight: '600px',
              minHeight: '500px',
              width: '100%',
              '& .MuiDataGrid-columnSeparator': {
                display: 'none',
              },
              '& .MuiDataGrid-columnHeaders': {
                padding: 0,
              },
              '& .MuiDataGrid-columnHeader': {
                padding: 0,
              },
            }}
            autoHeight={false}
            disableColumnMenu={true}
            getRowId={(row) => row.clock}
            rows={filteredData}
            columns={columns}
          />
        ) : (
          <CircularProgress />
        )}
      </Box>
    </CustomModal>
  );
};

export default MatchStatsModal;
