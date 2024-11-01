import * as React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { alpha, Card, CardContent, Grid, IconButton } from '@mui/material';
import { useSession } from 'next-auth/react';
import CustomModal from '../CustomModal';
import { CloseOutlined } from '@mui/icons-material';
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import MarketButton from '../Market/MarketButton';
import { useEconomyRepository } from '@/pages/api/economy/useEconomyRepository';

interface BillboardModalProps {
    open: boolean;
    handleClose: () => void;
}

const BillboardModal: React.FC<BillboardModalProps> = ({
    open,
    handleClose,
}: BillboardModalProps) => {
    const { data: userData, status: sessionStatus } = useSession({
        required: true,
    });

    const handleCloseModal = () => {
        handleClose();
    };

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

    const { signBillboadDeal } = useEconomyRepository(userData?.user.teamId);

    return (
        <>
            <CustomModal onClose={handleCloseModal} open={open}>
                <Box>
                    <IconButton
                        sx={{
                            width: '24px',
                            height: '24px',
                            position: 'absolute',
                            left: '95%',
                            top: '3%',
                            background: '#E5E5E5',
                        }}
                        onClick={handleCloseModal}
                        aria-label="close">
                        <CloseOutlined
                            sx={{
                                color: '#4F4F4F',
                                width: '15px',
                                height: '15px',
                            }}
                        />
                    </IconButton>
                    <MonetizationOnIcon
                        fontSize="large"
                        sx={{ color: '#FF3F84' }}
                    />
                </Box>
                <Typography
                    variant="h6"
                    component="h2"
                    sx={{ fontSize: '20px' }}
                    textAlign={'center'}>
                    Choose your new Billboard Deal
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    Lorem ipsum que tu quieras para explicar como es el proceso
                    de compra de un jugador.{' '}
                </Typography>
                <Grid container spacing={3} mt={1} justifyContent="center">
                    {['Short', 'Medium', 'Long'].map((card, index) => (
                        <Grid item xs={12} sm={6} md={4} key={index}>
                            <Card
                                sx={{
                                    textAlign: 'center',
                                    transition: 'background 0.3s ease',
                                    '&:hover': {
                                        backgroundColor: alpha('#FF3F84', 0.1),
                                    },
                                }}>
                                <CardContent>
                                    <Typography
                                        variant="h6"
                                        component="h3"
                                        gutterBottom>
                                        <strong>{card}</strong>
                                    </Typography>
                                    <Typography
                                        variant="body2"
                                        color="text.secondary">
                                        Lorem ipsum que tu quieras para explicar
                                        como es el proceso de compra de un
                                        jugador.
                                    </Typography>

                                    <Typography variant="body2" sx={{ mt: 1 }}>
                                        <strong>Base Offer:</strong> $1000
                                    </Typography>

                                    <Typography variant="body2" sx={{ mt: 1 }}>
                                        <strong>Modifier:</strong> +-25%
                                    </Typography>

                                    <MarketButton
                                        sx={{ mt: 1 }}
                                        onClick={() => handleButtonClick(card)}>
                                        Choose offer
                                    </MarketButton>
                                </CardContent>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
            </CustomModal>
        </>
    );
};

export default BillboardModal;
