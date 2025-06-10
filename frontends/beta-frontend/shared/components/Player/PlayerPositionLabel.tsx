import {
  PlayerPosition,
  PlayerPositionColorNew,
} from '@/shared/models/player/PlayerPosition';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { Box } from '@mui/material';
import React from 'react';

interface PlayerPositionLabelProps {
  position: string;
}

const PlayerPositionLabel: React.FC<PlayerPositionLabelProps> = ({
  position,
}) => {
  const initials = getPositionInitials(position);

  return (
    <Box
      sx={{
        height: '100%',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}>
      <div
        style={{
          justifyContent: 'center',
          display: 'flex',
          alignItems: 'center',
          color: 'black',
          padding: '2px 8px',
          width: '42px',
          height: '24px',
          borderRadius: '5px',
          textAlign: 'center',
          background: PlayerPositionColorNew[position as PlayerPosition],
        }}>
        {initials}
      </div>
    </Box>
  );
};

export default PlayerPositionLabel;
