import React from 'react'
import { Select, MenuItem, FormControl, SelectChangeEvent, InputLabel, NativeSelect } from '@mui/material'

export function PlayerOrderSelect() {
  const [playerOrder, setPlayerOrder] = React.useState('')

  const handleChangePlayerOrder = (event: SelectChangeEvent) => {
    setPlayerOrder(event.target.value as string)
  }

  return (
    <FormControl sx={{ minWidth: 70, marginTop: '16px' }} size="small">
      <InputLabel id="po1-select-label">PO</InputLabel>
      <Select
        labelId="po1-select-label"
        id="playerOrder1-select"
        value={playerOrder}
        onChange={handleChangePlayerOrder}
        sx={{ marginBottom: '1rem' }}>
        <MenuItem value={'playerOrder1'}>playerOrder1</MenuItem>
        <MenuItem value={'playerOrder2'}>playerOrder2</MenuItem>
        <MenuItem value={'playerOrder3'}>playerOrder3</MenuItem>
      </Select>
    </FormControl>
  )
}
