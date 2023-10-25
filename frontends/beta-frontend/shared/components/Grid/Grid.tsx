import { DataGrid, GridCallbackDetails, GridColDef, GridEventListener, GridRowParams, MuiEvent } from '@mui/x-data-grid'
import Box from '@mui/material/Box'

interface CustomGridColDef extends GridColDef {
  headerName: object;
}

type GridProps<T> = {
  rows: T[]
  columns: CustomGridColDef[]
}

function Grid<T>({ rows, columns }: GridProps<T>) {
  return (
    <Box sx={{
      '& .super-app .MuiDataGrid-cellContent': {
        color: 'white',
        padding: '6px',
        fontWeight: '600',
        borderRadius: '8%',
      },
      '& .super-app.goalkeeper .MuiDataGrid-cellContent': {
        backgroundColor: '#A4BC10',
      },
      '& .super-app.defender .MuiDataGrid-cellContent': {
        backgroundColor: '#F68B29',
      },
      '& .super-app.middle .MuiDataGrid-cellContent': {
        backgroundColor: '#29B6F6',
      },
      '& .super-app.forward .MuiDataGrid-cellContent': {
        backgroundColor: '#E52323',
      },
    }}>
      <DataGrid autoHeight={true} rows={rows} columns={columns} hideFooter={true} />
    </Box>
  )
}

export default Grid
