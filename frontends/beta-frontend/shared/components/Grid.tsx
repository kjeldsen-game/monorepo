import * as React from 'react'
import { DataGrid } from '@mui/x-data-grid'
import type {} from '@mui/x-data-grid/themeAugmentation'
import Box from '@mui/material/Box'
import { width } from '@mui/system'

const rows = [
  { id: 1, col1: 'League', col2: '15', col3: '0', col4: '5', col5: '4', col6: '1', col7: '0', col8: '6.17' },
  { id: 2, col1: 'International', col2: '2', col3: '0', col4: '0', col5: '1', col6: '0', col7: '0', col8: '6' },
  { id: 3, col1: 'Friendly', col2: '0', col3: '0', col4: '0', col5: '0', col6: '0', col7: '0', col8: '/' },
]

const columns = [
  { field: 'col1', headerName: '' },
  { field: 'col2', headerName: 'GP' },
  { field: 'col3', headerName: 'Gls' },
  { field: 'col4', headerName: 'As' },
  { field: 'col5', headerName: 'Ta' },
  { field: 'col6', headerName: 'Crd' },
  { field: 'col7', headerName: 'MoM' },
  { field: 'col8', headerName: 'Rating' },
]

export default function Grid() {
  return (
    <Box sx={{}}>
      <DataGrid
        autoHeight={true}
        rows={rows}
        columns={columns}
        hideFooter={true}
        sx={{
          minWidth: '805px',
          width: 'auto',
        }}
      />
    </Box>
  )
}
