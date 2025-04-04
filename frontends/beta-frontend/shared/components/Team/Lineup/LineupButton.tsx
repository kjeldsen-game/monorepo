import React, { useState } from 'react';
import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import { alpha, Box, Button } from '@mui/material';
import { Player } from '@/shared/models/Player';
import { getPositionInitials, getSurname } from '@/shared/utils/PlayerUtils';
import { PlayerPositionColorNew } from '@/shared/models/PlayerPosition';
import LineupCloseButton from './LineupCloseButton';
import { flexCenter } from '@/shared/styles/common';
import { useLineupEdit } from '@/shared/contexts/LineupEditContext';

interface LineupButtonProps {
  position?: string;
  sx?: Object;
  player?: Player;
}

const LineupButton: React.FC<LineupButtonProps> = ({
  position,
  sx,
  player,
}) => {
  const { handleEdit, activePlayer, edit: editValue } = useLineupEdit();
  const [isHovered, setIsHovered] = useState<boolean>(false);
  const showExitButton = player && isHovered && !activePlayer;
  const borderStyle = activePlayer || !player ? 'dashed' : 'solid';

  const borderColor = player
    ? showExitButton || activePlayer
      ? '#FF3F84'
      : player.position != null
        ? alpha(PlayerPositionColorNew[player.position], 0.3)
        : '#0000004D'
    : activePlayer
      ? '#FF3F84 '
      : '#0000004D';

  return (
    <Button
      disabled={player || activePlayer ? false : true}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      sx={{
        opacity: player || activePlayer ? '100%' : '20%',
        margin: '4px',
        height: '36px',
        padding: '0',
        border: `1px ${borderStyle} ${borderColor}`,
        borderRadius: 0,
        ...flexCenter,
        ...sx,
      }}
      onClick={() => {
        handleEdit(activePlayer, player, position, false);
      }}>
      {player ? (
        <Box
          sx={{
            background:
              player.position != null
                ? alpha(PlayerPositionColorNew[player.position], 0.3)
                : '#0000004D',
            width: '120px',
            padding: '8px',
            height: '100%',
            ...flexCenter,
            justifyContent: 'start',
          }}>
          <div
            style={{
              color: 'black',
              width: '30px',
              fontSize: '10px',
              height: '24px',
              borderRadius: '5px',
              textAlign: 'center',
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              background:
                player.position != null
                  ? alpha(PlayerPositionColorNew[player.position], 0.3)
                  : '#0000004D',
            }}>
            {getPositionInitials(player.position)}
          </div>
          <div
            style={{
              color: 'black',
              fontSize: '12px',
              fontWeight: 'bold',
              paddingLeft: '4px',
              overflow: 'hidden',
              textOverflow: 'ellipsis',
              whiteSpace: 'nowrap',
              textTransform: 'capitalize',
            }}>
            {getSurname(player?.name)}
          </div>
          {showExitButton && (
            <LineupCloseButton
              handleCloseModal={(event) => {
                event.stopPropagation();
                handleEdit(player, undefined, undefined, true);
              }}
            />
          )}
        </Box>
      ) : (
        <Box
          sx={{
            background: 'white',
            width: '120px',
            height: '100%',
            display: 'flex',
            justifyContent: 'space-evenly',
            alignItems: 'center',
          }}>
          <div
            style={{
              color: 'white',
              width: '30px',
              height: '24px',
              borderRadius: '5px',
              textAlign: 'center',
              background: '#0000004D',
            }}>
            {getPositionInitials(position)}
          </div>
          <PeopleAltIcon sx={{ color: '#0000004D' }} />
        </Box>
      )}
    </Button>
  );
};

export default LineupButton;
