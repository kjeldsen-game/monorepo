import { Box, Typography } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { baseColumnConfig, leftColumnConfig } from './ColumnsConfig';
import Link from 'next/link';
import MarketButton from '../../Market/MarketButton';
import { useSession } from 'next-auth/react';
import { formatDateAndTime } from '@/shared/utils/DateUtils';
import LinkButton from '../../Common/LinkButton';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';

export const calendarColumns = (teamId?: string) => {
  console.log(teamId);
  const columns: GridColDef[] = [
    {
      field: 'match',
      renderHeader: () => <div>Match</div>,
      ...leftColumnConfig,
      maxWidth: 50,
      renderCell: (params: GridCellParams) => (
        <Box>{params.api.getRowIndex(params.id) + 1}</Box>
      ),
    },

    {
      field: 'home',
      renderHeader: () => <div>Home</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Link
          style={{
            paddingInline: '20px',
            color: params.row.home.id === teamId ? '#FF3F84' : 'black',
            fontWeight: params.row.home.id === teamId ? 'bold' : 'normal',
            textDecoration: 'none',
          }}
          passHref
          href={`/team/${params.row.home.id}`}>
          {params.row.home.name}
        </Link>
      ),
    },
    {
      field: 'date',
      renderHeader: () => <div>Date</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box>{formatDateAndTime(params.row.dateTime)}</Box>
      ),
    },
    {
      field: 'away',
      renderHeader: () => <div>Away</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Link
          style={{
            paddingInline: '20px',
            color: params.row.away.id === teamId ? '#FF3F84' : 'black',
            fontWeight: params.row.away.id === teamId ? 'bold' : 'normal',
            textDecoration: 'none',
          }}
          passHref
          href={`/team/${params.row.away.id}`}>
          {params.row.away.name}
        </Link>
      ),
    },
    {
      field: 'result',
      renderHeader: () => <div>Result</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box>
          {params.row.status === 'PLAYED' ? (
            <Link passHref href={`/match/report/${params.row.id}`}>
              {params.row.matchReport.homeScore}-
              {params.row.matchReport.awayScore}
            </Link>
          ) : (
            <Typography color={'#000000'}>
              {' '}
              {convertSnakeCaseToTitleCase(params.row.status)}
            </Typography>
          )}
        </Box>
      ),
    },
    {
      field: 'lineup',
      renderHeader: () => <div>Lineup</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => {
        const team =
          params.row.away.id === teamId
            ? params.row.away
            : params.row.home.id === teamId
              ? params.row.home
              : null;
        return team ? (
          team.specificLineup ? (
            <div>Custom</div>
          ) : (
            <div>Default</div>
          )
        ) : null;
      },
    },
    {
      field: 'actions',
      renderHeader: () => <div>Actions</div>,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) =>
        (params.row.away.id === teamId || params.row.home.id === teamId) &&
        params.row.status == 'SCHEDULED' && (
          <Box>
            <LinkButton link={`/match/lineup/${params.row.id}`}>
              Change Lineup
            </LinkButton>
          </Box>
        ),
    },
  ];

  return columns;
};
