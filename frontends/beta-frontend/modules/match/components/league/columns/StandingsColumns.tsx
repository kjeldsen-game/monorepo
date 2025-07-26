import { BaseTextColumn } from '@/shared/components/Grid/Columns/common/columns/BaseTextColumn';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import ColLink from '@/shared/components/Grid/Columns/common/components/ColLink';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { LeagueStats } from 'modules/match/types/LeagueResponses';

export const StandingsColumns = <T extends LeagueStats>(): GridColDef[] => {
  return [
    BaseTextColumn('Pos', (row: T) => row.position, 'left'),
    // TODO use common team column
    {
      field: 'team',
      renderHeader: () => <ColHeader header="Team" />,
      ...getColumnConfig(),
      renderCell: (params: GridCellParams) => (
        <ColLink
          children={params.row.name}
          urlValue={`/team/${params.row.id}`}
        />
      ),
    },
    BaseTextColumn('Wins', (row: T) => row.wins),
    BaseTextColumn('Draws', (row: T) => row.draws),
    BaseTextColumn('Losses', (row: T) => row.losses),
    BaseTextColumn('Games Played', (row: T) => row.gamesPlayed),
    {
      field: 'score',
      renderHeader: () => <ColHeader header="Score" />,
      ...getColumnConfig(),
      renderCell: (params: GridCellParams<T>) => (
        <Box>
          {params.row.goalsScored}-{params.row.goalsReceived}
        </Box>
      ),
    },
    BaseTextColumn('Points ', (row: T) => row.points, 'right'),
  ];
};
