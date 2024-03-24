import { GridCellParams, GridColDef } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import Link from 'next/link'

const inviteColumns = (): GridColDef[] => [
  {
    field: 'teamName',
    headerName: 'Team name',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (params: GridCellParams) => (
      <Link passHref href={`/team/${params.row.id}`}>
        {params.row.away.id}
      </Link>
    ),
  },
]

export { inviteColumns }
