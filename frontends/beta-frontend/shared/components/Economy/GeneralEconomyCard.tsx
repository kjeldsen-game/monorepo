import React, { useState } from 'react';
import {
  Alert,
  Box,
  Grid,
  Snackbar,
  SnackbarCloseReason,
  Typography,
} from '@mui/material';
import EconomyCard from './EconomyCard';
import MarketButton from '../Market/MarketButton';
import SellIcon from '@mui/icons-material/Sell';
import PricingDataItem from './PricingDataItem';
import { useEconomyRepository } from '@/pages/api/economy/useEconomyRepository';
import { useSession } from 'next-auth/react';
import { State } from 'swr';

interface GeneralEconomyCardProps {
  balance: number;
  pricing: any;
}

const GeneralEconomyCard: React.FC<GeneralEconomyCardProps> = ({
  balance,
  pricing,
}) => {
  const { data: userData } = useSession({
    required: true,
  });

  const [showAlert, setShowAlert] = useState({
    open: false,
    type: 'success',
    message: '',
  });

  const [editMode, setEditMode] = useState(false);
  const [pricingData, setPricingData] = useState(pricing);

  const { editPricing } = useEconomyRepository(userData?.accessToken);

  function getIndexByType(type: string): number {
    return pricing.findIndex((item) => item.type === type);
  }

  const handleRemoveButtonClick = (type: string, value: number) => {
    const index = getIndexByType(type);
    const updatedPricing = [...pricingData];
    updatedPricing[index].value = value - 1;
    setPricingData(updatedPricing);
  };

  const handleAddButtonClick = (type: string, value: number) => {
    const index = getIndexByType(type);
    const updatedPricing = [...pricingData];
    updatedPricing[index].value = value + 1;
    setPricingData(updatedPricing);
  };

  const handleEditButtonClick = async () => {
    if (editMode) {
      try {
        const response = await editPricing(pricingData);
        if (response.status == 500) {
          setShowAlert({
            open: true,
            message: response.message,
            type: 'error',
          });
        } else {
          setShowAlert({
            open: true,
            message: 'Pricing updated successfully',
            type: 'success',
          });
        }
      } catch (error) {
        console.error('Failed to update auction:', error);
      }
    }
    setEditMode(!editMode);
  };

  const handleClose = (
    event: React.SyntheticEvent | Event,
    reason?: SnackbarCloseReason,
  ) => {
    if (reason === 'clickaway') {
      return;
    }

    setShowAlert((prev) => ({
      ...prev,
      open: false,
    }));
  };

  return (
    <EconomyCard>
      <Snackbar
        onClose={handleClose}
        autoHideDuration={1000}
        open={showAlert.open}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={showAlert.type} sx={{ width: '100%' }}>
          {showAlert.message}
        </Alert>
      </Snackbar>
      <Box
        sx={{
          height: '100%',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'space-between',
        }}>
        <Box height={'80%'}>
          <Box>
            <SellIcon fontSize="large" sx={{ color: '#FF3F84' }} />
            <Box>
              <Typography variant="h5" fontWeight={'900'}>
                Team Balance
              </Typography>
              <Box
                padding={'10px'}
                display={'flex'}
                alignItems={'center'}
                justifyContent={'space-between'}>
                <Typography
                  color={'#54595E99'}
                  sx={{
                    fontSize: '20px',
                    paddingY: '10px',
                    textAlign: 'left',
                  }}>
                  Total Balance :
                </Typography>
                <Typography fontSize={16} color={'#A4BC10'}>
                  {balance.toLocaleString(undefined, {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })}
                  {' $'}
                </Typography>
              </Box>
            </Box>
            <Typography variant="h5" fontWeight={'900'}>
              Pricing
            </Typography>
          </Box>
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              alignItems: 'center',
              padding: '10px',
              fontSize: '20px',
            }}>
            <Grid container>
              {pricingData.map((price) => {
                return (
                  <PricingDataItem
                    key={price.type}
                    handleAddButton={handleAddButtonClick}
                    handleRemoveButton={handleRemoveButtonClick}
                    isEdit={editMode}
                    value={price.value}
                    title={price.type}
                  />
                );
              })}
            </Grid>
          </Box>
          <Box></Box>
        </Box>
        <MarketButton
          variant="outlined"
          onClick={() => handleEditButtonClick()}>
          {editMode ? 'Save' : 'Edit'} pricing
        </MarketButton>
      </Box>
    </EconomyCard>
  );
};

export default GeneralEconomyCard;
