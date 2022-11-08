import * as React from 'react'
import { DataGrid } from '@mui/x-data-grid'
import type {} from '@mui/x-data-grid/themeAugmentation'
import Box from '@mui/material/Box'

// const rows: GridRowsProp = [
const rows = [
  { id: 1, col1: 'Hello', col2: 'World' },
  { id: 2, col1: 'DataGridPro', col2: 'is Awesome' },
  { id: 3, col1: 'MUI', col2: 'is Amazing' },
  { id: 4, col1: 'MUI', col2: 'is Amazing' },
  { id: 5, col1: 'MUI', col2: 'is Amazing' },
  { id: 6, col1: 'MUI', col2: 'is Amazing' },
  { id: 7, col1: 'MUI', col2: 'is Amazing' },
]

// const columns: GridColDef[] = [
const columns = [
  { field: 'col1', headerName: 'Column 1', width: 150 },
  { field: 'col2', headerName: 'Column 2', width: 150 },
  { field: 'col3', headerName: 'Column 3', width: 150 },
  { field: 'col4', headerName: 'Column 4', width: 150 },
]

export default function PlayerGrid() {
  return (
    <Box
      sx={{
        width: '100%',
        minWidth: '600px',
      }}>
      <DataGrid autoHeight={true} rows={rows} columns={columns} />
    </Box>
  )
}
