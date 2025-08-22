import CustomButton from '@/shared/components/Common/CustomButton';
import { alpha, Card, CardContent, Grid, Typography } from '@mui/material';
import { IncomeMode, IncomePeriodicity } from 'modules/player/types/Economy';
import React from 'react';

interface SponsorDialogCardProps {
    mode: IncomeMode;
    name: string;
    base: number;
    bonus: number;
    type: IncomePeriodicity;
    handleButtonClick: (mode: IncomeMode, type: IncomePeriodicity) => void;
}

const SponsorDialogCard: React.FC<SponsorDialogCardProps> = ({
    mode,
    name,
    base,
    bonus,
    type,
    handleButtonClick,
}) => {
    return (
        <Grid size={{ xs: 12, sm: 6, md: 4 }} key={mode}>
            <Card
                sx={{
                    textAlign: 'center',
                    transition: 'background 0.3s ease',
                    '&:hover': {
                        backgroundColor: alpha('#FF3F84', 0.1),
                    },
                }}>
                <CardContent>
                    <Typography variant="h6" component="h3" gutterBottom>
                        <strong>{name}</strong>{' '}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        Lorem ipsum que tu quieras para explicar como es el proceso de
                        compra de un jugador.
                    </Typography>
                    <Typography variant="body2" sx={{ mt: 1 }}>
                        <strong>Base Offer: </strong>
                        {base.toLocaleString()}
                        {' $'}
                    </Typography>
                    <Typography variant="body2" sx={{ mt: 1 }}>
                        <strong>Win bonus: </strong>
                        {bonus.toLocaleString()}
                        {' $'}
                    </Typography>
                    <CustomButton
                        sx={{ mt: 1 }}
                        onClick={() => handleButtonClick(mode, type)}>
                        Choose offer
                    </CustomButton>
                </CardContent>
            </Card>
        </Grid>
    );
};

export default SponsorDialogCard;
