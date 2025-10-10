import { Player } from '@/shared/models/player/Player';
import { useLineupBuilder } from 'modules/player/hooks/logic/useLineupBuilder';
import React from 'react'
import LineupPlayerView from '../lineup/LineupPlayerView';
import { Dialog, DialogContent } from '@mui/material';
import CloseButton from '@/shared/components/Common/CloseButton';
import BenchPlayersView from '../lineup/BenchPlayersView';
import { Lineup } from 'modules/player/types/Lineup';

interface TeamLineupDialogProps {
    lineup: Lineup;
    bench: Player[];
    open: boolean;
    handleClose: () => void;
}

const TeamLineupDialog: React.FC<TeamLineupDialogProps> = ({ lineup, open, handleClose, bench }) => {

    return (
        <Dialog sx={{ display: { xs: 'block', sm: 'none' } }} open={open} onClose={handleClose} scroll={'body'} maxWidth={'sm'}>
            <CloseButton handleCloseModal={handleClose} />
            <DialogContent>
                <LineupPlayerView lineup={lineup} />
                <BenchPlayersView bench={bench} />
            </DialogContent>
        </Dialog >
    )
}

export default TeamLineupDialog