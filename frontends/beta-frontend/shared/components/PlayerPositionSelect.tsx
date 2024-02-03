import React from 'react'
import { Select, MenuItem, FormControl, SelectChangeEvent, InputLabel } from '@mui/material'
import { PlayerPosition } from '../models/PlayerPosition'

interface PlayerPositionSelectProps {
  onChange?: (value: PlayerPosition) => void
  value: PlayerPosition
}

const PlayerPositionSelect: React.FC<PlayerPositionSelectProps> = ({ onChange, value }) => {
  const handleChangePlayerOrder = (event: SelectChangeEvent<PlayerPosition>) => {
    onChange?.(event.target.value as PlayerPosition)
  }

  return (
    <FormControl sx={{ minWidth: 140, marginTop: '16px' }} size="small">
      <InputLabel id="pp-select-label">PP</InputLabel>
      <Select<PlayerPosition>
        labelId="pp-select-label"
        id="playerOrder1-select"
        value={value}
        onChange={handleChangePlayerOrder}
        sx={{ marginBottom: '1rem', minWidth: '140px' }}>
        {Object.values(PlayerPosition).map((pos) => {
          return (
            // TODO: improve key by adding player id to it to make unique
            <MenuItem key={`player-pos-${pos}`} value={pos}>
              {pos}
            </MenuItem>
          )
        })}
      </Select>
    </FormControl>
  )
}

export { PlayerPositionSelect }
