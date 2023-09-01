import { DataGrid, GridCallbackDetails, GridColDef, GridEventListener, GridRowParams, MuiEvent } from '@mui/x-data-grid'
import Box from '@mui/material/Box'
import { useContext } from 'react'
import { PlayerContext } from 'contexts/PlayerContext'

type GridProps<T> = {
  rows: T[]
  columns: GridColDef[]
}

function Grid<T>({ rows, columns }: GridProps<T>) {
  return (
    <Box>
      <DataGrid autoHeight={true} rows={rows} columns={columns} hideFooter={true} />
    </Box>
  )
}

export default Grid
