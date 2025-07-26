import React from 'react';
import { Box, Card, Typography } from '@mui/material';
import SellIcon from '@mui/icons-material/Sell';

import { IncomeMode, IncomePeriodicity } from '@/shared/models/player/Economy';
import BillboardCardDataItem from '../Items/BillboardCardDataItem';
import CustomButton from '../../Common/CustomButton';

interface SponsorCardProps {
  open: boolean;
  mode: IncomeMode | null;
  handleOpenModal: (type: IncomePeriodicity) => void;
  type: IncomePeriodicity;
}

const SponsorCard: React.FC<SponsorCardProps> = ({
  open,
  mode,
  handleOpenModal,
  type,
}) => {
  return (
    <Card
      sx={{ height: '100%', padding: 2 }}
      data-testid={`sponsor-card-${type?.toLowerCase()}`}
    >
      <Box
        height={'100%'}
        display={'flex'}
        flexDirection={'column'}
        justifyContent={'space-between'}>
        <Box
          sx={{
            height: '100%',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}>
          <SellIcon fontSize="large" sx={{ color: '#FF3F84' }} />
          <Typography variant="h5" fontWeight={'900'} textAlign={'center'}>
            {type} Sponsor
          </Typography>
          {mode === null ? (
            <>
              <Typography
                color={'#54595E99'}
                fontSize={'12'}
                sx={{
                  height: '100%',
                  display: 'flex',
                  alignItems: 'center',
                  textAlign: 'center',
                }}>
                You don't have selected Sposor, click on the button to select
                one!
              </Typography>
            </>
          ) : (
            <>
              <BillboardCardDataItem title="Mode" value={mode} />
            </>
          )}
        </Box>
        {mode === null ? (
          <CustomButton onClick={() => handleOpenModal(type)}>
            Choose Offer
          </CustomButton>
        ) : (
          <></>
        )}
      </Box>
    </Card>
  );
};

export default SponsorCard;
