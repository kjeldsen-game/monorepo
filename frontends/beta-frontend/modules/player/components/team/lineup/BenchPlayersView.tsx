import { Box, Grid, Typography, useMediaQuery } from '@mui/material'
import React from 'react'
import LineupButton from './LineupButton'
import { Player } from '@/shared/models/player/Player';
import { theme } from '@/libs/material/theme';

interface BenchPlayersViewProps {
    bench: Player[] | undefined;
}

const BenchPlayersView: React.FC<BenchPlayersViewProps> = ({ bench }) => {
    const isXs = useMediaQuery(theme.breakpoints.down("sm"));

    return (
        <Box
            borderTop={isXs && '1px #0000004D solid'}
            paddingTop={isXs && 1}
            borderLeft={!isXs && '1px #0000004D solid'}
            display={'flex'}
            flexDirection={'column'}
            alignItems={'center'}
            justifyContent={'center'}>
            <Box gap={1} display={'flex'} flexWrap={'wrap'} sx={{ justifyContent: 'center' }}>
                {Array.from({ length: 7 }).map((_, index) => (
                    <LineupButton key={index} player={bench?.[index] || undefined} />
                ))}
            </Box>
        </Box >
    )
}

export default BenchPlayersView