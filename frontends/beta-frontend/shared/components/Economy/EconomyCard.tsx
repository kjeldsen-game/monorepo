import { Card, Grid, SxProps } from '@mui/material';
import React from 'react';

interface EconomyCardProps {
    children?: React.ReactNode;
    sx?: SxProps;
}

const EconomyCard: React.FC<EconomyCardProps> = ({
    children,
    sx,
}: EconomyCardProps) => {
    return (
        <Grid item xs={12} sm={6} md={4} sx={{ padding: '20px' }}>
            <Card
                sx={{
                    minHeight: '500px',
                    textAlign: 'center',
                    height: '100%',
                    padding: '16px',
                    ...sx,
                }}>
                {children}
            </Card>
        </Grid>
    );
};

export default EconomyCard;
