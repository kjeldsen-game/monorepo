import { DataGrid, GridCallbackDetails, GridColDef, GridEventListener, GridRowParams, MuiEvent } from '@mui/x-data-grid'
import Box from '@mui/material/Box'
import { useRouter } from 'next/router'


type GridProps<T> = {
  rows: T[]
  columns: GridColDef[]
}

function Grid<T>({ rows, columns }: GridProps<T>) {
  const router = useRouter()

  const handleRowClick: GridEventListener<'rowClick'> = (params) => {
    router.push(`/player/${params.row.id}`)

    console.log(params)
    console.log(params.row.id)
  }

  return (
    <Box>
      <DataGrid autoHeight={true} rows={rows} columns={columns} hideFooter={true} onRowClick={handleRowClick} />
    </Box>
  )
}

export default Grid
