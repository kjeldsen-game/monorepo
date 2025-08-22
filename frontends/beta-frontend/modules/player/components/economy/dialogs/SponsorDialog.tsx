import {
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
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import SponsorDialogCard from '../cards/SponsorDialogCard';
import { IncomeMode, IncomePeriodicity } from 'modules/player/types/Economy';
import { SignSponsorRequest } from 'modules/player/types/Requests';
import { getEnumKeyByValue } from '@/shared/utils/EnumUtils';
import { useEconomyApi } from 'modules/player/hooks/api/useEconomyApi';

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
    const offers = SPONSORS_OFFERS[type];

    const { handleSponsorSign } = useEconomyApi()

    const handleButtonClick = async (mode: string, periodicity: string) => {
        const request: SignSponsorRequest = {
            mode: getEnumKeyByValue(IncomeMode, mode), periodicity: getEnumKeyByValue(IncomePeriodicity, periodicity)
        }
        handleSponsorSign(request, handleClose)
    };

    return (
        <Dialog open={open} onClose={handleClose} scroll={'body'} maxWidth={'md'}>
            <DialogTitle
                display={'flex'}
                flexDirection={'column'}
                alignItems={'center'}
                justifyItems={'center'}>
                <MonetizationOnIcon fontSize="large" sx={{ color: '#FF3F84' }} />
                <Typography
                    variant="h3"
                    component={'div'}
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
                                    key={modeKey}
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
