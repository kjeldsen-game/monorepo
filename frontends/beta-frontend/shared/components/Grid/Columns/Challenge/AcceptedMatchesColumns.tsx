import { Box, MenuItem, Select } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from '../ColumnsConfig';
import { formatDateAndTime } from '@/shared/utils/DateUtils';
import MarketButton from '../../../Market/MarketButton';
import LinkButton from '../../../Common/LinkButton';
import ColHeader from '../Common/ColHeader';
import ColLink from '../Common/ColLink';

const acceptedMatchesColumns = (
  handlePlayButtonClick: (matchId: string) => void,
  teamId: string,
): GridColDef[] => [
  {
    field: 'away',
    ...leftColumnConfig,
    renderHeader: () => <ColHeader header="Away" align={'left'} />,
    renderCell: (params: GridCellParams) => (
      <ColLink
        children={params.row.away.name}
        urlValue={`/team/${params.row.away.id}`}
      />
    ),
  },
  {
    field: 'home',
    ...baseColumnConfig,
    renderHeader: () => <ColHeader header="Home" />,
    renderCell: (params: GridCellParams) => (
      <ColLink
        children={params.row.home.name}
        urlValue={`/team/${params.row.home.id}`}
      />
    ),
  },
  // {
  //   field: 'dateTime',
  //   renderHeader: () => <ColHeader header="Date" />,
  //   ...baseColumnConfig,
  //   renderCell: (params: GridCellParams) => {
  //     return formatDateAndTime(params.row?.dateTime);
  //   },
  // },
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
    renderCell: (params: GridCellParams) => {
      return (
        <>
          {params.row.status === 'ACCEPTED' &&
            params.row.home.id === teamId && (
              <MarketButton
                sx={{ marginRight: '8px' }}
                onClick={() => handlePlayButtonClick(params.row.id)}>
                Play
              </MarketButton>
            )}

          {params.row.away.name === params.row.home.name ? (
            <Box>
              <LinkButton
                sx={{ marginLeft: '8px' }}
                link={`/match/self/${params.row.id}`}>
                Set lineups
              </LinkButton>
            </Box>
          ) : (
            <Box>
              <LinkButton link={`/match/lineup/${params.row.id}`}>
                Change Lineup
              </LinkButton>
            </Box>
          )}
        </>
      );
    },
  },
];

export default acceptedMatchesColumns;
