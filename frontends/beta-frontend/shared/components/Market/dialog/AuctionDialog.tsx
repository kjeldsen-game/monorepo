import { AuctionMarket } from '@/shared/models/market/Auction';
import { Dialog, DialogContent, DialogTitle, Typography } from '@mui/material'
import React from 'react'
import LocalAtmIcon from '@mui/icons-material/LocalAtm';
import { useSession } from 'next-auth/react';
import AuctionData from './AuctionData';
import AuctionBidForm from './AuctionBidForm';
import CloseButton from '../../Common/CloseButton';

interface AuctionDialogProps {
    open: boolean;
    handleClose: () => void;
    auction?: AuctionMarket | undefined;
}

const AuctionDialog: React.FC<AuctionDialogProps> = ({ open, handleClose, auction }) => {

    const { data: userData } = useSession({
        required: true,
    });

    return (
        <Dialog open={open} onClose={handleClose} scroll={'body'} maxWidth={'xs'}>
            <CloseButton handleCloseModal={handleClose} />
            <DialogTitle display={'flex'} flexDirection={'column'} alignItems={'center'} justifyItems={'center'}>
                <LocalAtmIcon fontSize="large" sx={{ color: '#FF3F84' }} />
                <Typography variant="h6">
                    Bid for player
                </Typography>
            </DialogTitle>
            <DialogContent>
                <Typography variant="body2" color="text.secondary" textAlign={'center'}>
                    Lorem ipsum que tu quieras para explicar como es el proceso de compra de un jugador
                </Typography>
                <AuctionData auction={auction} />
                {auction?.teamId === userData?.user.teamId ? (
                    <Typography
                        sx={{
                            color: '#FF3F84',
                            padding: '8px',
                            textAlign: 'center',
                        }}
                        variant="body2"
                    > You are the owner of this player </Typography>
                ) : (
                    <AuctionBidForm auctionId={auction?.id} />
                )}
            </DialogContent>
        </Dialog >
    )
}

export default AuctionDialog