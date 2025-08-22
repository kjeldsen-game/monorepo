import { Box, Grid } from '@mui/material'
import React from 'react'
import LineupButton from './LineupButton';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { Lineup } from 'modules/player/types/Lineup';

interface LineupPlayerViewProps {
    lineup: Lineup;
}

const LineupPlayerView: React.FC<LineupPlayerViewProps> = ({ lineup }) => {
    return (
        <Box display={'flex'} width={{ xs: '100%', sm: 'auto' }} padding={1} flexDirection={{ xs: 'column-reverse', sm: 'row' }} alignItems={'center'}>
            <Grid container mt={{ xs: 1, sm: 0 }} spacing={1} flexDirection={{ xs: 'row', sm: 'column' }}>
                <LineupButton position={PlayerPosition.GOALKEEPER} player={lineup?.GOALKEEPER} />
            </Grid>
            <Grid container mt={{ xs: 1, sm: 0 }} ml={{ sm: 1, xs: 0 }} spacing={1} size={12} flexDirection={{ xs: 'row', sm: 'column' }}>
                <LineupButton position={PlayerPosition.LEFT_BACK} player={lineup?.LEFT_BACK} />
                {lineup.CENTRE_BACK?.map((player, index) => (
                    <LineupButton key={index} position={PlayerPosition.CENTRE_BACK} player={player} />
                ))}
                <LineupButton position={PlayerPosition.RIGHT_BACK} player={lineup?.RIGHT_BACK} />
            </Grid>
            <Grid container mt={{ xs: 1, sm: 0 }} ml={{ sm: 1, xs: 0 }} spacing={1} flexDirection={{ xs: 'row', sm: 'column' }}>
                <LineupButton sx={{ mb: { xs: 0, sm: 2 } }} position={PlayerPosition.LEFT_WINGBACK} player={lineup?.LEFT_WINGBACK} />
                {lineup.DEFENSIVE_MIDFIELDER?.map((player, index) => (
                    <LineupButton key={index} position={PlayerPosition.DEFENSIVE_MIDFIELDER} player={player} />
                ))}
                <LineupButton sx={{ mt: { xs: 0, sm: 2 } }} position={PlayerPosition.RIGHT_WINGBACK} player={lineup?.RIGHT_WINGBACK} />
            </Grid>
            <Grid container mt={{ xs: 1, sm: 0 }} ml={{ sm: 1, xs: 0 }} spacing={1} flexDirection={{ xs: 'row', sm: 'column' }}>
                <LineupButton position={PlayerPosition.LEFT_MIDFIELDER} player={lineup?.LEFT_MIDFIELDER} />
                {lineup.CENTRE_MIDFIELDER?.map((player, index) => (
                    <LineupButton key={index} position={PlayerPosition.CENTRE_MIDFIELDER} player={player} />
                ))}
                <LineupButton position={PlayerPosition.RIGHT_MIDFIELDER} player={lineup?.RIGHT_MIDFIELDER} />
            </Grid>
            <Grid container mt={{ xs: 1, sm: 0 }} ml={{ sm: 1, xs: 0 }} spacing={1} flexDirection={{ xs: 'row', sm: 'column' }}>
                <LineupButton sx={{ mb: { xs: 0, sm: 2 } }} position={PlayerPosition.LEFT_WINGER} player={lineup?.LEFT_WINGER} />
                {lineup.OFFENSIVE_MIDFIELDER?.map((player, index) => (
                    <LineupButton key={index} position={PlayerPosition.OFFENSIVE_MIDFIELDER} player={player} />
                ))}
                <LineupButton sx={{ mt: { xs: 0, sm: 2 } }} position={PlayerPosition.RIGHT_WINGER} player={lineup?.RIGHT_WINGER} />
            </Grid>
            <Grid container spacing={1} ml={{ sm: 1, xs: 0 }} flexDirection={{ xs: 'row', sm: 'column' }}>
                {lineup.FORWARD?.map((player, index) => (
                    <LineupButton key={index} position={PlayerPosition.FORWARD} player={player} />
                ))}
            </Grid>
        </Box>
    )
}

export default LineupPlayerView