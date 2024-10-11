import React from 'react'
import { Select, MenuItem, FormControl, SelectChangeEvent } from '@mui/material'
import { PlayerLineupStatus } from '../models/PlayerLineupStatus'
import { useTranslation } from 'next-i18next'

interface PlayerStatusSelectProps {
  onChange?: (value: PlayerLineupStatus) => void
  value: PlayerLineupStatus
}

const PlayerStatusSelect: React.FC<PlayerStatusSelectProps> = ({ onChange, value }) => {
  const { t } = useTranslation(['game'])

  const handleChangePlayerStatus = (event: SelectChangeEvent<PlayerLineupStatus>) => {
    onChange?.(event.target.value as PlayerLineupStatus)
  }

  return (
    <FormControl sx={{ minWidth: 80, marginTop: '16px' }} size="small">
      <Select<PlayerLineupStatus>
        labelId="p-status-select-label"
        id="playerStatus-select"
        value={value}
        renderValue={() => t('game:playerValues.lineupStatus.' + value)}
        onChange={handleChangePlayerStatus}
        sx={{ marginBottom: '1rem', minWidth: '80px'}}>
        {Object.values(PlayerLineupStatus).map((pos) => {
          return (
            // TODO: improve key by adding player id to it to make unique
            <MenuItem key={`player-status-${pos}`} value={pos}>
              {pos}
            </MenuItem>
          )
        })}
      </Select>
    </FormControl>
  )
}

export { PlayerStatusSelect }
