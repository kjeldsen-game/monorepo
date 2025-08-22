import { Box, Grid, useMediaQuery } from '@mui/material'
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
            padding={'8px'}
            borderLeft={!isXs && '1px #0000004D solid'}
            display={'flex'}
            flexDirection={'column'}
            alignItems={'center'}
            justifyContent={'center'}>
            <Grid container spacing={2}>
                <Grid>
                    <LineupButton player={bench?.[0] || undefined} />
                    <LineupButton sx={{ my: 1 }} player={bench?.[1] || undefined} />
                    <LineupButton player={bench?.[2] || undefined} />
                </Grid>
                <Grid>
                    <LineupButton player={bench?.[3] || undefined} />
                    <LineupButton sx={{ my: 1 }} player={bench?.[4] || undefined} />
                    <LineupButton player={bench?.[5] || undefined} />
                </Grid>
            </Grid>
            <LineupButton sx={{ mt: 1 }} player={bench?.[6] || undefined} />
        </Box>
    )
}

export default BenchPlayersView