import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from '../ColumnsConfig';
import ColHeader from '../Common/ColHeader';
import ColLink from '../Common/ColLink';

export const standingsColumns = () => {
  const columns: GridColDef[] = [
    {
      field: 'position',
      renderHeader: () => <ColHeader header={'Pos'} align={'left'} />,
      ...leftColumnConfig,
      maxWidth: 50,
      renderCell: (params: GridCellParams) => (
        <Box sx={{ paddingLeft: '10px' }}>{params.row.position}</Box>
      ),
    },
    {
      field: 'team',
      renderHeader: () => <ColHeader header={'Team'} />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <ColLink
          children={params.row.name}
          urlValue={`/team/${params.row.id}`}
        />
      ),
    },
    {
      field: 'wins',
      renderHeader: () => <ColHeader header={'Wins'} />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => <Box>{params.row.wins}</Box>,
    },
    {
      field: 'draws',
      renderHeader: () => <ColHeader header={'Draws'} />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => <Box>{params.row.draws}</Box>,
    },
    {
      field: 'losses',
      renderHeader: () => <ColHeader header={'Losses'} />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => <Box>{params.row.losses}</Box>,
    },
    {
      field: 'gamesPlayerd',
      renderHeader: () => <ColHeader header={'Games Played'} />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box>{params.row.gamesPlayed}</Box>
      ),
    },
    {
      field: 'score',
      renderHeader: () => <ColHeader header={'Score'} />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box>
          {params.row.goalsScored}-{params.row.goalsReceived}
        </Box>
      ),
    },
    {
      field: 'points',
      renderHeader: () => <ColHeader header={'Points'} align={'right'} />,
      ...rightColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box sx={{ paddingRight: '10px' }}>{params.row.points}</Box>
      ),
    },
  ];

  return columns;
};
