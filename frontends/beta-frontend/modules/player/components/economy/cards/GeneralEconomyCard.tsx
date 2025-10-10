import React, { useState } from 'react';
import {
  Box,
  Grid,
  Typography,
} from '@mui/material';
import EconomyCard from './EconomyCard';
import SellIcon from '@mui/icons-material/Sell';
import { useEconomyRepository } from '@/pages/api/economy/useEconomyRepository';
import { useSession } from 'next-auth/react';
import PricingDataItem from '../items/PricingDataItem';
import CustomButton from '@/shared/components/Common/CustomButton';

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

  const [editMode, setEditMode] = useState(false);
  const [pricingData, setPricingData] = useState(pricing);

  const { editPricing } = useEconomyRepository(
    userData?.user.teamId,
    userData?.accessToken,
  );

  function getIndexByType(type: string): number {
    return pricing?.findIndex((item) => item.type === type);
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

  const handleEditButtonClick = () => {
    if (editMode) {
      editPricing(pricingData);
    }
    setEditMode(!editMode);
  };

  return (
    <EconomyCard>
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
              <Grid container>
                <Grid size={{ lg: 6, sm: 12, xs: 6 }} sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Typography
                    color={'#54595E99'}
                    sx={{
                      fontSize: '20px',
                      paddingY: '10px',
                    }}>
                    Total Balance :
                  </Typography>
                </Grid>
                <Grid size={{ lg: 6, sm: 12, xs: 6 }} sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Typography fontSize={16} color={'#A4BC10'}>
                    {balance?.toLocaleString(undefined, {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                    {' $'}
                  </Typography>
                </Grid>
              </Grid>
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
            {/* <Grid container>
              {pricingData.length > 0 &&
                pricingData.map((price) => (
                  <PricingDataItem
                    key={price?.type}
                    handleAddButton={handleAddButtonClick}
                    handleRemoveButton={handleRemoveButtonClick}
                    isEdit={editMode}
                    value={price?.value}
                    title={price?.type}
                  />
                ))
              }
            </Grid> */}
          </Box>
        </Box>
        <CustomButton
          variant="outlined"
          onClick={() => handleEditButtonClick()}>
          {editMode ? 'Save' : 'Edit'} pricing
        </CustomButton>
      </Box>
    </EconomyCard>
  );
};

export default GeneralEconomyCard;
