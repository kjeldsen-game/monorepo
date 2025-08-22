import { Dialog, DialogContent, DialogTitle, Grid, SxProps, Typography } from '@mui/material'
import React, { ReactNode } from 'react'
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import BillboardDialogCard from '../cards/BillboardDialogCard';
import { IncomeMode } from 'modules/player/types/Economy';
import { SignBillboardRequest } from 'modules/player/types/Requests';
import { getEnumKeyByValue } from '@/shared/utils/EnumUtils';
import { useEconomyApi } from 'modules/player/hooks/api/useEconomyApi';

interface BillboardDialogProps {
    open: boolean;
    handleClose: () => void;
    children?: ReactNode;
    sx?: SxProps;
}

const BillboardDialog: React.FC<BillboardDialogProps> = ({
    open,
    handleClose,
}) => {
    const { handleBillboardSign } = useEconomyApi();

    const handleButtonClick = async (mode: string) => {
        const request: SignBillboardRequest = {
            mode: getEnumKeyByValue(IncomeMode, mode)
        }
        handleBillboardSign(request, handleClose)
    };

    return (
        <Dialog open={open} onClose={handleClose} scroll={'body'} maxWidth={'md'}>
            <DialogTitle display={'flex'} flexDirection={'column'} alignItems={'center'} justifyItems={'center'}>
                <MonetizationOnIcon fontSize="large" sx={{ color: '#FF3F84' }} />
                <Typography
                    variant="h6"
                    component="h2"
                    sx={{ fontSize: '20px' }}
                    textAlign={'center'}>
                    Choose your new Billboard Deal
                </Typography>
            </DialogTitle>
            <DialogContent>
                <Typography variant="body2" color="text.secondary" textAlign={'center'}>
                    Lorem ipsum que tu quieras para explicar como es el proceso de compra
                    de un jugador.{' '}
                </Typography>
                <Grid container spacing={3} mt={1} justifyContent="center">
                    {['Short', 'Medium', 'Long'].map((card, index) => (
                        <Grid size={{ xs: 12, sm: 6, md: 4 }} key={index}>
                            <BillboardDialogCard type={card} handleButtonClick={handleButtonClick} />
                        </Grid>
                    ))}
                </Grid>
            </DialogContent>
        </Dialog>
    )
}

export default BillboardDialog