import { Box, Typography } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from '../ColumnsConfig';
import { formatDateAndTime } from '@/shared/utils/DateUtils';
import LinkButton from '../../../Common/LinkButton';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import ColHeader from '../Common/ColHeader';
import ColLink from '../Common/ColLink';

export const calendarColumns = (teamId?: string) => {
  const getLinkStyle = (isAwayTeam: boolean): React.CSSProperties => ({
    color: isAwayTeam ? '#FF3F84' : 'black',
    fontWeight: isAwayTeam ? 'bold' : 'normal',
  });

  const columns: GridColDef[] = [
    {
      field: 'match',
      renderHeader: () => <ColHeader header={'Match'} align={'left'} />,
      ...leftColumnConfig,
      maxWidth: 80,
      renderCell: (params: GridCellParams) => (
        <Box sx={{ paddingLeft: '10px' }}>
          {params.api.getRowIndex(params.id) + 1}
        </Box>
      ),
    },

    {
      field: 'home',
      renderHeader: () => <ColHeader header={'Home'} />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <ColLink
          sx={getLinkStyle(params.row.home.id === teamId)}
          urlValue={`/team/${params.row.home.id}`}>
          {params.row.home.name}
        </ColLink>
      ),
    },
    {
      field: 'date',
      renderHeader: () => <ColHeader header="Date" />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box>{formatDateAndTime(params.row.dateTime)}</Box>
      ),
    },
    {
      field: 'away',
      renderHeader: () => <ColHeader header="Away" />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <ColLink
          sx={getLinkStyle(params.row.away.id === teamId)}
          urlValue={`/team/${params.row.away.id}`}>
          {params.row.away.name}
        </ColLink>
      ),
    },
    {
      field: 'result',
      renderHeader: () => <ColHeader header="Result" />,
      ...baseColumnConfig,
      renderCell: (params: GridCellParams) => (
        <Box>
          {params.row.status === 'PLAYED' ? (
            <ColLink urlValue={`/match/report/${params.row.id}`}>
              {params.row.matchReport.homeScore}-
              {params.row.matchReport.awayScore}
            </ColLink>
          ) : (
            <Typography color={'#000000'}>
              {convertSnakeCaseToTitleCase(params.row.status)}
            </Typography>
          )}
        </Box>
      ),
    },
    {
      field: 'lineup',
      renderHeader: () => <ColHeader header="Lineup" />,
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
      renderHeader: () => <ColHeader header="Actions" align={'right'} />,
      ...rightColumnConfig,
      renderCell: (params: GridCellParams) =>
        (params.row.away.id === teamId || params.row.home.id === teamId) &&
        params.row.status == 'SCHEDULED' && (
          <LinkButton link={`/match/lineup/${params.row.id}`}>
            Change Lineup
          </LinkButton>
        ),
    },
  ];

  return columns;
};
