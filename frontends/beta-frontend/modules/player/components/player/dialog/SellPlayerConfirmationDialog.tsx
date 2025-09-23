import CloseButton from '@/shared/components/Common/CloseButton'
import { Box, Dialog, DialogContent, DialogTitle, Typography } from '@mui/material'
import React from 'react'
import LocalAtmIcon from '@mui/icons-material/LocalAtm';
import CustomButton from '@/shared/components/Common/CustomButton';
import { usePlayerApi } from 'modules/player/hooks/api/usePlayerApi';

interface SellPlayerConfirmationDialogProps {
    open: boolean;
    handleClose: () => void;
    playerId: string
}

const SellPlayerConfirmationDialog: React.FC<SellPlayerConfirmationDialogProps> = ({ open, handleClose, playerId }) => {

    const { handleSellPlayerRequest } = usePlayerApi(playerId);

    return (
        <Dialog open={open} onClose={handleClose} scroll={'body'} maxWidth={'xs'}>
            <CloseButton handleCloseModal={handleClose} />
            <DialogTitle display={'flex'} flexDirection={'column'} alignItems={'center'} justifyItems={'center'}>
                <LocalAtmIcon fontSize="large" sx={{ color: '#FF3F84' }} />
                <Typography variant="h6">
                    Sell Player
                </Typography>
            </DialogTitle>
            <DialogContent>
                <Typography variant="body2" color="text.secondary" textAlign={'center'}>
                    Lorem ipsum que tu quieras para explicar como es el proceso de compra de un jugador
                </Typography>
                <Box sx={{ textAlign: 'center' }}>
                    <Typography>
                        Are you sure that you want to sell player?
                    </Typography>
                </Box>
                <CustomButton
                    data-testid={"sell-button"}
                    sx={{ mt: 2, width: '100%' }}
                    onClick={() => {
                        handleSellPlayerRequest();
                        handleClose();
                    }}>
                    Sell Player
                </CustomButton>
            </DialogContent>
        </Dialog >
    )
}

export default SellPlayerConfirmationDialog