import {
    IncomePeriodicity,
    SPONSORS_OFFERS,
} from '@/shared/models/player/Economy';
import {
    Dialog,
    DialogContent,
    DialogTitle,
    Grid,
    Typography,
} from '@mui/material';
import React from 'react';
import { useSession } from 'next-auth/react';
import { useEconomyRepository } from '@/pages/api/economy/useEconomyRepository';
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import SponsorDialogCard from '../Cards/SponsorDialogCard';

interface SponsorDialogProps {
    open: boolean;
    handleClose: () => void;
    type: IncomePeriodicity;
}

const SponsorDialog: React.FC<SponsorDialogProps> = ({
    open,
    handleClose,
    type,
}) => {
    const { data: userData } = useSession({
        required: true,
    });

    const offers = SPONSORS_OFFERS[type];

    const handleButtonClick = async (mode: string, periodicity: string) => {
        try {
            const response = await signSponsor(periodicity, mode);
            if (response.status == 500) {
            } else {
                handleClose();
            }
        } catch (error) {
            console.error('Failed to update auction:', error);
        }
    };

    const { signSponsor } = useEconomyRepository(
        userData?.user.teamId,
        userData?.accessToken,
    );

    return (
        <Dialog open={open} onClose={handleClose} scroll={'body'} maxWidth={'md'}>
            <DialogTitle
                display={'flex'}
                flexDirection={'column'}
                alignItems={'center'}
                justifyItems={'center'}>
                <MonetizationOnIcon fontSize="large" sx={{ color: '#FF3F84' }} />
                <Typography
                    variant="h6"
                    component="h2"
                    sx={{ fontSize: '20px' }}
                    textAlign={'center'}>
                    Choose your new {type} Sponsor
                </Typography>
            </DialogTitle>
            <DialogContent>
                <Typography variant="body2" color="text.secondary" textAlign={'center'}>
                    Lorem ipsum que tu quieras para explicar como es el proceso de compra
                    de un jugador.{' '}
                </Typography>
                <Grid container spacing={3} mt={1} justifyContent="center">
                    {offers && Object.keys(offers).length > 0 ? (
                        Object.keys(offers).map((modeKey) => {
                            const { base, bonus } = offers[modeKey as IncomeMode];
                            const modeName = modeKey;

                            return (
                                <SponsorDialogCard
                                    mode={modeKey}
                                    base={base}
                                    bonus={bonus}
                                    handleButtonClick={handleButtonClick}
                                    name={modeName}
                                    type={type}
                                />
                            );
                        })
                    ) : (
                        <Typography variant="body2" color="text.secondary">
                            No offers available.
                        </Typography>
                    )}
                </Grid>
            </DialogContent>
        </Dialog>
    );
};

export default SponsorDialog;
