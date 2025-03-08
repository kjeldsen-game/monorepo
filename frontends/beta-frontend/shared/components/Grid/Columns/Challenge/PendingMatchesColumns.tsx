import { Box, Button } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from '../ColumnsConfig';
import { formatDateAndTime } from '@/shared/utils/DateUtils';
import ColLink from '../Common/ColLink';
import ColHeader from '../Common/ColHeader';

const PendingMatchesColumns = (
  ownTeamId: string,
  onChallengeAccept: (matchID: string) => void,
  onChallengeDecline: (matchID: string) => void,
): GridColDef[] => [
  {
    field: 'enemy',
    ...leftColumnConfig,
    renderHeader: () => <ColHeader header="Enemy" align={'left'} />,
    renderCell: ({ row }: GridCellParams) => {
      const enemyTeam = row.home.id === ownTeamId ? row.away : row.home;
      return (
        <ColLink children={enemyTeam.name} urlValue={`/team/${enemyTeam.id}`} />
      );
    },
  },
  {
    field: 'dateTime',
    renderHeader: () => <ColHeader header="Date" />,
    ...baseColumnConfig,
    renderCell: (params: GridCellParams) => {
      return formatDateAndTime(params.row?.dateTime);
    },
  },
  {
    field: 'acceptButton',
    renderHeader: () => <ColHeader header="Action" align={'right'} />,
    ...rightColumnConfig,
    renderCell: (params: GridCellParams) => {
      if (ownTeamId === params.row?.home?.id)
        return (
          <Button variant="contained" disabled color="info" sx={{ mx: '10px' }}>
            Pending confirmation
          </Button>
        );

      return (
        <Box>
          <Button
            variant="contained"
            color="info"
            sx={{ mx: '10px' }}
            onClick={() => onChallengeAccept(params.row?.id)}>
            Accept
          </Button>
          <Button
            variant="contained"
            color="error"
            sx={{ mx: '10px' }}
            onClick={() => onChallengeDecline(params.row?.id)}>
            Decline
          </Button>
        </Box>
      );
    },
  },
];

export default PendingMatchesColumns;
