import { DataGrid, GridCallbackDetails, GridColDef, GridEventListener, GridRowParams, MuiEvent } from '@mui/x-data-grid'
import Box from '@mui/material/Box'
import { useContext } from 'react'
import css from 'styled-jsx/css'

type GridProps<T> = {
  rows: T[]
  columns: GridColDef[]
}

function Grid<T>({ rows, columns }: GridProps<T>) {
  return (
    <Box sx={{
      '& .super-app.goalkeeper .MuiDataGrid-cellContent': {
        backgroundColor: '#A4BC10',
        color: 'white',
        padding: '6px',
        fontWeight: '600',
        borderRadius: '8%',
      },
      '& .super-app.defender .MuiDataGrid-cellContent': {
        backgroundColor: '#F68B29',
        color: 'white',
        padding: '6px',
        fontWeight: '600',
        borderRadius: '8%',
      },
      '& .super-app.middle .MuiDataGrid-cellContent': {
        backgroundColor: '#29B6F6',
        color: 'white',
        padding: '6px',
        fontWeight: '600',
        borderRadius: '8%',
      },
      '& .super-app.forward .MuiDataGrid-cellContent': {
        backgroundColor: '#E52323',
        color: 'white',
        padding: '6px',
        fontWeight: '600',
        borderRadius: '8%',
      },
    }}>
      <DataGrid autoHeight={true} rows={rows} columns={columns} hideFooter={true} />
    </Box>
  )
}

export default Grid
