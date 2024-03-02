import { GridCellParams, GridColDef, GridValueGetterParams } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import Link from 'next/link'

const leagueColumns: GridColDef[] = [
  {
    field: 'manager',
    headerName: 'Manager',
    headerAlign: 'center' as GridAlignment,
    minWidth: 130,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.id,
  },
  {
    field: 'teamName',
    headerName: 'Team name',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (params: GridCellParams) => (
      <Link passHref href={`/league/team/${params.row.id}`}>
        {params.row.name}
      </Link>
    ),
  },
]

export { leagueColumns }
