import React from 'react'
import { Select, MenuItem, FormControl, SelectChangeEvent, InputLabel } from '@mui/material'
import { PlayerOrder } from '../models/PlayerOrder'

export function PlayerOrderSelect() {
  const [playerOrder, setPlayerOrder] = React.useState('')

  const handleChangePlayerOrder = (event: SelectChangeEvent) => {
    setPlayerOrder(event.target.value as string)
  }

  return (
    <FormControl sx={{ minWidth: 140, marginTop: '16px' }} size="small">
      <InputLabel id="po1-select-label">PO</InputLabel>
      <Select
        labelId="po1-select-label"
        id="playerOrder1-select"
        value={playerOrder}
        onChange={handleChangePlayerOrder}
        sx={{ marginBottom: '1rem' }}>
        {Object.values(PlayerOrder).map((order) => {
          return (
            // TODO: improve key by adding player id to it to make unique
            <MenuItem key={`player-order-${order}`} value={order}>
              {order}
            </MenuItem>
          )
        })}
      </Select>
    </FormControl>
  )
}
