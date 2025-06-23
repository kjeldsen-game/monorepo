import { Dialog, DialogContent, DialogTitle, Grid, SxProps, Typography } from '@mui/material'
import React, { ReactNode } from 'react'
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import { useEconomyRepository } from '@/pages/api/economy/useEconomyRepository';
import { useSession } from 'next-auth/react';
import BillboardDialogCard from './BillboardDialogCard';

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
    const { data: userData, status: sessionStatus } = useSession({
        required: true,
    });

    const handleButtonClick = async (mode: string) => {
        try {
            const response = await signBillboadDeal(mode);
            if (response.status == 500) {
            } else {
                handleClose();
            }
        } catch (error) {
            console.error('Failed to update auction:', error);
        }
    };

    const { signBillboadDeal } = useEconomyRepository(
        userData?.user.teamId,
        userData?.accessToken,
    );

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