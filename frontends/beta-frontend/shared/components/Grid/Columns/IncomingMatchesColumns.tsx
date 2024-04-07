import { MenuItem, Select } from '@mui/material'
import { GridCellParams, GridColDef } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import Link from 'next/link'

const incomingMatchesColumns = (): GridColDef[] => [
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
    field: 'lineupButton',
    headerName: 'Lineup',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: () => {
      return (
        <Select
          variant="standard"
          color="info"
          sx={{ mx: '5px', height: '40px' }}
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          label="Lineup"
          value={0}>
          <MenuItem value={0}>Default</MenuItem>
          <MenuItem value={1}>Specific lineup...</MenuItem>
        </Select>
      )
    },
  },
]

export { incomingMatchesColumns }
export default incomingMatchesColumns
