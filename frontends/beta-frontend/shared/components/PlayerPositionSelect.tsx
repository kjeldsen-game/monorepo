import React from 'react'
import { Select, MenuItem, FormControl, SelectChangeEvent } from '@mui/material'
import { PlayerPosition, PlayerPositionColor } from '../models/PlayerPosition'
import { useTranslation } from 'next-i18next'

interface PlayerPositionSelectProps {
  onChange?: (value: PlayerPosition) => void
  value: PlayerPosition
}

const PlayerPositionSelect: React.FC<PlayerPositionSelectProps> = ({ onChange, value }) => {
  const { t } = useTranslation(['game'])

  const handleChangePlayerOrder = (event: SelectChangeEvent<PlayerPosition>) => {
    onChange?.(event.target.value as PlayerPosition)
  }

  return (
    <FormControl sx={{ minWidth: 80, marginTop: '16px' }} size="small">
      <Select<PlayerPosition>
        labelId="pp-select-label"
        id="playerOrder1-select"
        value={value}
        renderValue={() => t('game:playerValues.positionDiminutives.' + value)}
        onChange={handleChangePlayerOrder}
        sx={{ marginBottom: '1rem', minWidth: '80px', backgroundColor: PlayerPositionColor[value] }}>
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
