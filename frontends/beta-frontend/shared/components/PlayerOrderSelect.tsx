import React from 'react';
import {
  Select,
  MenuItem,
  FormControl,
  SelectChangeEvent,
  InputLabel,
} from '@mui/material';
import { PlayerOrder } from '../models/PlayerOrder';
import SelectInput from './Common/SelectInput';
import { PITCH_AREAS_ENUM } from '../models/PitchArea';

interface PlayerOrderSelectProps {
  onChange?: (value: PlayerOrder) => void;
  value: PlayerOrder;
}

export const PlayerOrderSelect: React.FC<PlayerOrderSelectProps> = ({
  onChange,
  value,
}) => {
  const handleChangePlayerOrder = (event: SelectChangeEvent<PlayerOrder>) => {
    onChange?.(event.target.value as PlayerOrder);
  };

  return (
    // <SelectInput>

    // </SelectInput>
    <FormControl sx={{ marginTop: '16px' }} size="small">
      <InputLabel id="po1-select-label">PO</InputLabel>
      <Select<PlayerOrder>
        labelId="po1-select-label"
        id="playerOrder1-select"
        value={value}
        onChange={handleChangePlayerOrder}
        sx={{ marginBottom: '1rem' }}>
        {Object.values(PlayerOrder).map((order) => {
          return (
            // TODO: improve key by adding player id to it to make unique
            <MenuItem key={`player-order-${order}`} value={order}>
              {order}
            </MenuItem>
          );
        })}
      </Select>
    </FormControl>
  );
};
