import { Box, Button } from '@mui/material'
import { GridCellParams, GridColDef } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import Link from 'next/link'

const inviteColumns = (): GridColDef[] => [
  {
    field: 'id',
    headerName: 'Match Id',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (params: GridCellParams) => (
      <Link passHref href={`/team/${params.row.id}`}>
        {params.row?.away?.id}
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
    renderCell: () => (
      <Box>
        <Button variant="contained" color="info" sx={{ mx: '5px' }}>
          Accept
        </Button>
        <Button variant="contained" color="error" sx={{ mx: '5px' }}>
          Decline
        </Button>
      </Box>
    ),
  },
]

export { inviteColumns }
export default inviteColumns
