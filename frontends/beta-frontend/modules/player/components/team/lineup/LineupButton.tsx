import { theme } from '@/libs/material/theme';
import { Player } from '@/shared/models/player/Player';
import { PlayerPosition, PlayerPositionColorNew } from '@/shared/models/player/PlayerPosition';
import { flexCenter } from '@/shared/styles/common';
import { alpha, Box, Button, SxProps, useMediaQuery } from '@mui/material';
import { useLineupEdit } from 'modules/player/contexts/LineupContext';
import React, { useState } from 'react'
import LineupButtonText from './LineupButtonText';
import PlayerPositionLabel from '@/shared/components/Player/PlayerPositionLabel';
import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import CloseButton from '@/shared/components/Common/CloseButton';

interface LineupButtonProps {
    position?: PlayerPosition;
    sx?: SxProps;
    player?: Player;
}

const LineupButton: React.FC<LineupButtonProps> = ({
    position,
    sx,
    player,
}) => {
    const { handleEdit, activePlayer } = useLineupEdit();
    const [isHovered, setIsHovered] = useState<boolean>(false);
    const isXs = useMediaQuery(theme.breakpoints.down("sm"))

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
            component="div"
            disabled={player || activePlayer ? false : true}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
            sx={{
                background: player
                    ? player.position
                        ? alpha(PlayerPositionColorNew[player.position], 0.2)
                        : '#0000004D'
                    : player?.position
                        ? 'white'
                        : 'white',
                fontSize: '10px',
                fontWeight: 'bold',
                minWidth: '50px',
                minHeight: isXs && '50px',
                opacity: player || activePlayer ? '100%' : '20%',
                border: `1px ${borderStyle} ${borderColor}`,
                ...flexCenter,
                padding: '0px',
                ...sx,
            }}
            onClick={
                () => handleEdit(activePlayer, player, position, false)
            }
        >
            <Box
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'start',
                    ...!isXs ? { width: '120px', padding: '4px 8px' } : { width: 'auto', padding: 0 },
                    flexDirection: isXs ? "column" : 'row',
                }}>
                {showExitButton &&
                    <CloseButton
                        sx={{
                            ...(isXs
                                && {
                                left: "50%",
                                top: "50%",
                                transform: "translate(-50%, -50%)",
                            }
                            )
                        }}
                        variant={'filled'}
                        handleCloseModal={(event) => {
                            event.stopPropagation();
                            handleEdit(player, undefined, undefined, true);
                        }} />}
                <PlayerPositionLabel
                    sx={{
                        width: ' auto',
                        paddingX: '8px',
                        ...!player ? { background: theme.palette.quaternary.main, color: 'white' } : {}
                    }}
                    position={player?.position || player?.preferredPosition || position} />
                {player ? <LineupButtonText player={player} /> :
                    <PeopleAltIcon sx={{ color: theme.palette.quaternary.main, paddingLeft: '4px' }} />}
            </Box>
        </Button>
    )
}

export default LineupButton