import LinkButton from '@/shared/components/Common/LinkButton';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import ColLink from '@/shared/components/Grid/Columns/common/components/ColLink';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import { Box, Typography } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import SwapHorizIcon from '@mui/icons-material/SwapHoriz';
import IconLinkButton from '@/shared/components/Common/IconLinkButton';
import { DateTimeColumn } from '../../challenge/columns/common/DateTimeColumn';

export const CalendarColumns = (teamId?: string) => {
  const getLinkStyle = (isAwayTeam: boolean): React.CSSProperties => ({
    color: isAwayTeam ? '#FF3F84' : 'black',
    fontWeight: isAwayTeam ? 'bold' : 'normal',
  });

  const columns: GridColDef[] = [
    {
      field: 'match',
      maxWidth: 25,
      width: 25,
      minWidth: 25,
      renderHeader: () => <ColHeader header={''} align={'left'} />,
      ...getColumnConfig('left'),
      renderCell: (params: GridCellParams) => (
        <Box sx={{ paddingLeft: '10px' }}>
          {params.api.getRowIndexRelativeToVisibleRows(params.id) + 1}
        </Box>
      ),
    },
    // TeamNameColumn((row) => row.home, 'center', "Home"),
    {
      field: 'home',
      renderHeader: () => <ColHeader header={'Home'} />,
      ...getColumnConfig(),
      renderCell: (params: GridCellParams) => (
        <ColLink
          sx={getLinkStyle(params.row.home.id === teamId)}
          urlValue={`/team/${params.row.home.id}`}>
          {params.row.home.name}
        </ColLink>
      ),
    },
    DateTimeColumn((row) => row.dateTime, 'center', "Date"),
    {
      field: 'away',
      renderHeader: () => <ColHeader header="Away" />,
      ...getColumnConfig(),
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
      ...getColumnConfig(),
      renderCell: (params: GridCellParams) => (
        <Box>
          {params.row.status === 'PLAYED' ? (
            <ColLink urlValue={`/match/report/${params.row.id}`}>
              {params.row.matchReport.homeScore}-
              {params.row.matchReport.awayScore}
            </ColLink>
          ) : (
            <Box>
              {/* {convertSnakeCaseToTitleCase(params.row.status)} */}
            </Box>
          )}
        </Box>
      ),
    },
    {
      field: 'lineup',
      renderHeader: () => <ColHeader header="Lineup" />,
      ...getColumnConfig(),
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
      renderHeader: () => <ColHeader header="Action" align={'right'} />,
      ...getColumnConfig('right'),
      renderCell: (params: GridCellParams) =>
        (params.row.away.id === teamId || params.row.home.id === teamId) &&
        params.row.status == 'SCHEDULED' && (
          <Box sx={{ display: 'flex', alignItems: 'center', marginRight: '10px' }} justifyContent={'end'} height={'100%'}>
            <IconLinkButton variant='outlined' link={`/match/lineup/${params.row.id}`}>
              <SwapHorizIcon />
            </IconLinkButton>
            {/* <LinkButton link={`/match/lineup/${params.row.id}`}>
              Lineup
            </LinkButton> */}
          </Box>
        ),
    },
  ];

  return columns;
};
