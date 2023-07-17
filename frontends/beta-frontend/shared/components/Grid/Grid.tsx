import { DataGrid, GridCallbackDetails, GridColDef, GridEventListener, GridRowParams, MuiEvent } from '@mui/x-data-grid'
import Box from '@mui/material/Box'

type GridProps<T> = {
  rows: T[]
  columns: GridColDef[]
  // handleRowClick?: (params: GridRowParams, event: MuiEvent, details: GridCallbackDetails) => void
}

const handleRowClick: GridEventListener<'rowClick'> = (params) => {
  // setMessage(`Movie "${params.row.title}" clicked`)
  console.log(params)
}

function Grid<T>({ rows, columns }: GridProps<T>) {
  return (
    <Box>
      <DataGrid autoHeight={true} rows={rows} columns={columns} hideFooter={true} onRowClick={handleRowClick} />
    </Box>
  )
}

export default Grid
