import * as React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import {
  alpha,
  Button,
  Card,
  CardContent,
  Grid,
  IconButton,
} from '@mui/material';
import { useSession } from 'next-auth/react';
import CustomModal from '../CustomModal';
import { CloseOutlined } from '@mui/icons-material';
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import MarketButton from '../Market/MarketButton';
import {
  IncomeMode,
  IncomePeriodicity,
  SPONSORS_OFFERS,
} from '@/shared/models/Economy';
import { useEconomyRepository } from '@/pages/api/economy/useEconomyRepository';

interface SponsorsModalProps {
  open: boolean;
  handleClose: () => void;
  type: IncomePeriodicity;
}

const SponsorsModal: React.FC<SponsorsModalProps> = ({
  open,
  handleClose,
  type = IncomePeriodicity.ANNUAL,
}: SponsorsModalProps) => {
  const { data: userData } = useSession({
    required: true,
  });

  const offers = SPONSORS_OFFERS[type];
  const handleCloseModal = () => {
    handleClose();
  };

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

  const { signSponsor } = useEconomyRepository(userData?.accessToken);

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
          <MonetizationOnIcon fontSize="large" sx={{ color: '#FF3F84' }} />
        </Box>
        <Typography
          variant="h6"
          component="h2"
          sx={{ fontSize: '20px' }}
          textAlign={'center'}>
          Choose your new {type} Sponsor
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Lorem ipsum que tu quieras para explicar como es el proceso de compra
          de un jugador.{' '}
        </Typography>
        <Grid container spacing={3} mt={1} justifyContent="center">
          {offers && Object.keys(offers).length > 0 ? (
            Object.keys(offers).map((modeKey) => {
              const { base, bonus } = offers[modeKey as IncomeMode];
              const modeName = modeKey;

              return (
                <Grid item xs={12} sm={6} md={4} key={modeKey}>
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
                        <strong>{modeName}</strong>{' '}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Lorem ipsum que tu quieras para explicar como es el
                        proceso de compra de un jugador.
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
                      <MarketButton
                        sx={{ mt: 1 }}
                        onClick={() => handleButtonClick(modeName, type)}>
                        Choose offer
                      </MarketButton>
                    </CardContent>
                  </Card>
                </Grid>
              );
            })
          ) : (
            <Typography variant="body2" color="text.secondary">
              No offers available.
            </Typography>
          )}
        </Grid>
      </CustomModal>
    </>
  );
};

export default SponsorsModal;
