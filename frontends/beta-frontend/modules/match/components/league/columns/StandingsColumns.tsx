import { BaseTextColumn } from '@/shared/components/Grid/Columns/common/columns/BaseTextColumn';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import ColLink from '@/shared/components/Grid/Columns/common/components/ColLink';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { LeagueStats } from 'modules/match/types/LeagueResponses';
import { TeamNameColumn } from '../../columns/TeamNameColumn';

export const StandingsColumns = <T extends LeagueStats>(): GridColDef[] => {
  return [
    BaseTextColumn('Pos', (row: T) => row.position, 'left'),
    TeamNameColumn((row) => row, 'center', "Team"),
    BaseTextColumn('Wins', (row: T) => row.wins, 'center', undefined, "W"),
    BaseTextColumn('Draws', (row: T) => row.draws, 'center', undefined, "D"),
    BaseTextColumn('Losses', (row: T) => row.losses, 'center', undefined, "L"),
    BaseTextColumn('Games Played', (row: T) => row.gamesPlayed, 'center', undefined, "GP"),
    {
      field: 'score',
      renderHeader: () => <ColHeader header="Score" secondaryHeader='SC' />,
      ...getColumnConfig(),
      renderCell: (params: GridCellParams<T>) => (
        <Box>
          {params.row.goalsScored}-{params.row.goalsReceived}
        </Box>
      ),
    },
    BaseTextColumn('Points ', (row: T) => row.points, 'right', undefined, "Pts"),
  ];
};
