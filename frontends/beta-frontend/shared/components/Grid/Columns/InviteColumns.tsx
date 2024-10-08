import { Box, Button } from '@mui/material'
import { GridCellParams, GridColDef } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import Link from 'next/link'
import { sampleColumns } from '../PlayerGrid'

const inviteColumns = (
  ownTeamId: string,
  onChallengeAccept: (matchID: string) => void,
  onChallengeDecline: (matchID: string) => void,
): GridColDef[] => [
  {
    field: 'id',
    headerName: 'Match Id',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (params: GridCellParams) => (
      <Link passHref href={`/team/${params.row.away}`}>
        {params.value}
      </Link>
    ),
  },
  {
    field: 'dateTime',
    headerName: 'Date',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
  },
  {
    field: 'acceptButton',
    headerName: 'Action',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (params: GridCellParams) => {
      if (ownTeamId === params.row?.home?.id)
        return (
          <Button variant="contained" disabled color="info" sx={{ mx: '5px' }}>
            Pending confirmation
          </Button>
        )

      if (params.row?.status === 'ACCEPTED')
        return (
          <Button variant="contained" disabled color="success" sx={{ mx: '5px' }}>
            Accepted
          </Button>
        )

      return (
        <Box>
          <Button variant="contained" color="info" sx={{ mx: '5px' }} onClick={() => onChallengeAccept(params.row?.id)}>
            Accept
          </Button>
          <Button variant="contained" color="error" sx={{ mx: '5px' }} onClick={() => onChallengeDecline(params.row?.id)}>
            Decline
          </Button>
        </Box>
      )
    },
  },
]

export { inviteColumns }
export default inviteColumns
