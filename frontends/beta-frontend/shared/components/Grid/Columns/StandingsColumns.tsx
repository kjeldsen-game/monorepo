import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { baseColumnConfig, leftColumnConfig } from './ColumnsConfig';
import Link from 'next/link';

export const standingsColumns = () => {
  const columns: GridColDef[] = [
    {
      field: 'position',
      renderHeader: () => <div>Pos</div>,
      ...leftColumnConfig,
      maxWidth: 50,
      renderCell: (params: GridCellParams) => <Box>{params.row.position}</Box>,
    },
    {
      field: 'team',
      renderHeader: () => <div>Team</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Link
          style={{
            paddingInline: '20px',
            color: 'black',
            fontWeight: 'bold',
            textDecoration: 'none',
          }}
          passHref
          href={`/team/${params.row.id}`}>
          {params.row.name}
        </Link>
      ),
    },
    {
      field: 'wins',
      renderHeader: () => <div>Wins</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => <Box>{params.row.wins}</Box>,
    },
    {
      field: 'draws',
      renderHeader: () => <div>Draws</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => <Box>{params.row.draws}</Box>,
    },
    {
      field: 'losses',
      renderHeader: () => <div>Losses</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => <Box>{params.row.losses}</Box>,
    },
    {
      field: 'gamesPlayerd',
      renderHeader: () => <div>Games Played</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box>{params.row.gamesPlayed}</Box>
      ),
    },
    {
      field: 'score',
      renderHeader: () => <div>Score</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box>
          {params.row.goalsScored}-{params.row.goalsReceived}
        </Box>
      ),
    },
    {
      field: 'points',
      renderHeader: () => <div>Points</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => <Box>{params.row.points}</Box>,
    },
  ];

  return columns;
};
