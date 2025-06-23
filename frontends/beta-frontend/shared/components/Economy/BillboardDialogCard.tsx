import { alpha, Card, CardContent, Typography } from '@mui/material'
import React from 'react'
import MarketButton from '../Market/MarketButton'

interface BillboardDialogCardProps {
    type: string
    handleButtonClick: (type: string) => void;
}

const BillboardDialogCard: React.FC<BillboardDialogCardProps> = ({ type, handleButtonClick }) => {
    return (
        <Card
            sx={{
                textAlign: 'center',
                '&:hover': {
                    backgroundColor: alpha('#FF3F84', 0.1),
                },
            }}>
            <CardContent>
                <Typography variant="h6" component="h3" gutterBottom>
                    <strong>{type}</strong>
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    Lorem ipsum que tu quieras para explicar como es el proceso
                    de compra de un jugador.
                </Typography>

                <Typography variant="body2" sx={{ mt: 1 }}>
                    <strong>Base Offer:</strong> $1000
                </Typography>

                <Typography variant="body2" sx={{ mt: 1 }}>
                    <strong>Modifier:</strong> +-25%
                </Typography>

                <MarketButton
                    sx={{ mt: 1 }}
                    onClick={() => handleButtonClick(type)}>
                    Choose offer
                </MarketButton>
            </CardContent>
        </Card>
    )
}

export default BillboardDialogCard