import { DataGrid, DataGridProps, GridColDef } from '@mui/x-data-grid'
import Box from '@mui/material/Box'

type GridProps<T> = {
  rows: T[]
  columns: GridColDef[]
} & DataGridProps

function Grid<T>({ rows, columns, ...props }: GridProps<T>) {
  return (
    <Box
      sx={{
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
      <DataGrid autoHeight={true} rows={rows} columns={columns} hideFooter={true} {...props} />
    </Box>
  )
}

export default Grid
