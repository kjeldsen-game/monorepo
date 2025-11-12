import { Player } from '@/shared/models/player/Player';
import { LineupProvider } from 'modules/player/contexts/LineupContext';
import React from 'react'
import { Box, Typography, useMediaQuery } from '@mui/material';
import { theme } from '@/libs/material/theme';
import { useLineupBuilder } from 'modules/player/hooks/logic/useLineupBuilder';
import BenchPlayersView from './BenchPlayersView';
import LineupPlayerView from './LineupPlayerView';
import TeamLineupDialog from '../dialogs/TeamLineupDialog';
import { Rating } from 'modules/player/types/Player';

interface LineupViewProps {
    rating: Rating;
    players: Player[];
    edit: boolean;
    activePlayer: Player;
    handleEdit: (
        newPlayer: Player,
        oldPlayer?: Player,
        position?: string,
        inactive?: boolean,
    ) => void;
    open: boolean;
    handleCloseModal: () => void;
}

const LineupView: React.FC<LineupViewProps> = ({rating, players, edit, activePlayer, handleEdit, handleCloseModal, open }) => {

    const { lineup, bench } = useLineupBuilder(players);
    const isXs = useMediaQuery(theme.breakpoints.down("sm"));

    return (
        <LineupProvider
            handleEdit={handleEdit}
            activePlayer={activePlayer}
            edit={edit}>
            <Box flexDirection={'column'} display={'flex'} sx={{alignItems: 'center'}}>
                <Typography fontWeight={'bold'} color={theme.palette.quaternary.main}>Total Team Rating</Typography>
                <Typography fontWeight={'bold'} color={theme.palette.quaternary.main}>{rating?.actual}/{rating?.potential}</Typography>
            </Box>
            <Box display={'flex'}
                flexDirection={{ xs: 'column', sm: 'row' }}
                width={'100%'}
                justifyContent={'center'}
                minHeight={isXs && '363px'}>
                <LineupPlayerView lineup={lineup} />
                <BenchPlayersView bench={bench} />
            </Box>
            <TeamLineupDialog
                lineup={lineup}
                bench={bench}
                open={open}
                handleClose={handleCloseModal}
            />
        </LineupProvider>
    )
}

export default LineupView