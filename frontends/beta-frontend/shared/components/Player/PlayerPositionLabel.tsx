import {
  PlayerPosition,
  PlayerPositionColorNew,
} from '@/shared/models/PlayerPosition';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import React from 'react';

interface PlayerPositionLabelProps {
  position: string;
}

const PlayerPositionLabel: React.FC<PlayerPositionLabelProps> = ({
  position,
}) => {
  const initials = getPositionInitials(position);

  return (
    <div
      style={{
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
  );
};

export default PlayerPositionLabel;
