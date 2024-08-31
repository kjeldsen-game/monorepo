import { Button, MenuItem, Select } from '@mui/material'
import { GridCellParams, GridColDef } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import Link from 'next/link'

const pastMatchesColumns = (onReportSelect: (matchId: string) => void): GridColDef[] => [
  {
    field: 'away',
    headerName: 'away',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (params: GridCellParams) => (
      <Link passHref href={`/team/${params.row.away.id}`}>
        {params.row.away.name}
      </Link>
    ),
  },
  {
    field: 'home',
    headerName: 'home',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (params: GridCellParams) => (
      <Link passHref href={`/team/${params.row.home.id}`}>
        {params.row.home.name}
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
    field: 'reportButton',
    headerName: 'Report',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (val) => {
      console.log(val.row)
      return (
        <Button variant="contained" color="primary" onClick={() => onReportSelect(val.row.id)}>
          Report
        </Button>
      )
    },
  },
]

export { pastMatchesColumns }
export default pastMatchesColumns
