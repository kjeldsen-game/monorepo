import { Box, Button } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from './ColumnsConfig';
import {
  formatDateAndTime,
  formatDateToDDMMYY,
} from '@/shared/utils/DateUtils';

const inviteColumns = (
  ownTeamId: string,
  onChallengeAccept: (matchID: string) => void,
  onChallengeDecline: (matchID: string) => void,
): GridColDef[] => [
  {
    field: 'id',
    ...leftColumnConfig,
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Match ID</div>,
    renderCell: (params: GridCellParams) => (
      <Link
        style={{
          paddingInline: '20px',
          color: 'black',
          textDecoration: 'none',
        }}
        passHref
        href={`/team/${params.row.away.id}`}>
        {params.value}
      </Link>
    ),
  },
  {
    field: 'dateTime',
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Date</div>,
    ...baseColumnConfig,
    renderCell: (params: GridCellParams) => {
      return formatDateAndTime(params.row?.dateTime);
    },
  },
  {
    field: 'acceptButton',
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Action</div>,
    ...rightColumnConfig,
    renderCell: (params: GridCellParams) => {
      if (params.row?.status === 'ACCEPTED')
        return (
          <Button
            variant="contained"
            disabled
            color="success"
            sx={{ mx: '10px' }}>
            Accepted
          </Button>
        );

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

export { inviteColumns };
export default inviteColumns;
