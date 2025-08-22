import { Player } from '@/shared/models/player/Player';
import { LineupProvider } from 'modules/player/contexts/LineupContext';
import React, { useState } from 'react'
import { Box, useMediaQuery } from '@mui/material';
import { theme } from '@/libs/material/theme';
import AutorenewIcon from '@mui/icons-material/Autorenew';
import CustomIconButton from '@/shared/components/Common/CustomIconButton';
import { useLineupBuilder } from 'modules/player/hooks/logic/useLineupBuilder';
import BenchPlayersView from './BenchPlayersView';
import LineupPlayerView from './LineupPlayerView';

interface LineupViewProps {
    players: any;
    edit: boolean;
    activePlayer: Player;
    handleEdit: (
        newPlayer: Player,
        oldPlayer?: Player,
        position?: string,
        inactive?: boolean,
    ) => void;
}

const LineupView: React.FC<LineupViewProps> = ({ players, edit, activePlayer, handleEdit }) => {

    const { lineup, bench } = useLineupBuilder(players);
    const [isLineupActive, setIsLineupActive] = useState<boolean>(true);
    const isXs = useMediaQuery(theme.breakpoints.down("sm"));

    const toggleIsLineupActive = () => {
        setIsLineupActive(prev => !prev);
    };

    return (
        <>
            <LineupProvider
                handleEdit={handleEdit}
                activePlayer={activePlayer}
                edit={edit}>
                {isXs &&
                    <CustomIconButton onClick={toggleIsLineupActive}>
                        <AutorenewIcon />
                    </CustomIconButton>}

                <Box display={'flex'} width={'100%'} justifyContent={'center'} minHeight={isXs && '363px'}>
                    {(!isXs || (isXs && isLineupActive)) && (
                        <LineupPlayerView lineup={lineup} />
                    )}
                    {(!isXs || (isXs && !isLineupActive)) && (
                        <BenchPlayersView bench={bench} />
                    )}
                </Box>
            </LineupProvider>
        </>
    )
}

export default LineupView