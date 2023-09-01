import { DataGrid, GridCallbackDetails, GridColDef, GridEventListener, GridRowParams, MuiEvent } from '@mui/x-data-grid'
import Box from '@mui/material/Box'
import { useContext } from 'react'
import { PlayerContext } from 'contexts/PlayerContext'

type GridProps<T> = {
  rows: T[]
  columns: GridColDef[]
}

function Grid<T>({ rows, columns }: GridProps<T>) {
  // const playerContext = useContext(PlayerContext);

  // const handleRowClick: GridEventListener<'rowClick'> = (params) => {
  //   // playerContext?.setSelectedPlayer(params.row)
  //   // event?.preventDefault()
  //   // router.push({pathname: `/player/${params.row.id}`, query: params.row})
  //   // console.log(params)
  //   // console.log(params.row)
  // }

  return (
    <Box>
      <DataGrid autoHeight={true} rows={rows} columns={columns} hideFooter={true} />
    </Box>
  )
}

export default Grid
