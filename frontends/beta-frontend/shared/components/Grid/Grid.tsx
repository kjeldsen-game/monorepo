import { DataGrid, GridCallbackDetails, GridColDef, GridEventListener, GridRowParams, MuiEvent } from '@mui/x-data-grid'
import Box from '@mui/material/Box'
import Link from 'next/link'

type GridProps<T> = {
  rows: T[]
  columns: GridColDef[]
  // handleRowClick?: (params: GridRowParams, event: MuiEvent, details: GridCallbackDetails) => void
}

const handleRowClick: GridEventListener<'rowClick'> = (params) => {
  <Link href={`/players/${params.row.id}`}></Link>

  console.log(params)
  console.log(params.row.id)
}

function Grid<T>({ rows, columns }: GridProps<T>) {
  return (
    <Box>
      <DataGrid autoHeight={true} rows={rows} columns={columns} hideFooter={true} onRowClick={handleRowClick} />
    </Box>
  )
}

export default Grid
