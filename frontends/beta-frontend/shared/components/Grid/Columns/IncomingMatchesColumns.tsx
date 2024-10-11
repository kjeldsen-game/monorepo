import { MenuItem, Select } from '@mui/material'
import { GridCellParams, GridColDef } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import Link from 'next/link'

const incomingMatchesColumns = (handleLineupChange: (value: number, teamId: string) => void): GridColDef[] => [
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
    field: 'lineupButton',
    headerName: 'Lineup',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (val) => {
      return (
        <Select
          variant="standard"
          color="info"
          sx={{ mx: '5px', height: '40px' }}
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          label="Lineup"
          value={0}
          onChange={(item) => handleLineupChange(item.target.value as number, val.row.id)}>
          <MenuItem value={0}>Default</MenuItem>
          <MenuItem value={1}>Specific lineup...</MenuItem>
        </Select>
      )
    },
  },
]

export { incomingMatchesColumns }
export default incomingMatchesColumns
