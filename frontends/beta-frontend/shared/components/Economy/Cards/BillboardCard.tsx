import React from 'react';
import { Box, Grid, Typography } from '@mui/material';
import SellIcon from '@mui/icons-material/Sell';
import WarningIcon from '@mui/icons-material/Warning';
import BillboardCardDataItem from '../Items/BillboardCardDataItem';
import MarketButton from '../../Market/MarketButton';
import EconomyCard from './EconomyCard';

interface BillboardCardProps {
  billboardDeal: any;
  setOpen: (isOpen: boolean) => void;
}

const BillboardCard: React.FC<BillboardCardProps> = ({
  billboardDeal,
  setOpen,
}) => {
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
            <Typography variant="h5" fontWeight={'900'}>
              Billboard Deal
            </Typography>
          </Box>

          {billboardDeal != undefined || billboardDeal != null ? (
            <Box
              sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                padding: '10px',
                fontSize: '20px',
              }}>
              <Grid container>
                <BillboardCardDataItem
                  title={'Type'}
                  value={billboardDeal.type}
                />
                <BillboardCardDataItem
                  title={'Offer'}
                  isMoney={true}
                  value={billboardDeal.offer}
                />
                <BillboardCardDataItem
                  title={'Valid from Season'}
                  value={billboardDeal.startSeason}
                />
                <BillboardCardDataItem
                  title={'Valid to Season'}
                  value={billboardDeal.endSeason}
                />
              </Grid>
            </Box>
          ) : (
            <Box
              sx={{
                height: '100%',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                flexDirection: 'column',
                padding: '10px',
              }}>
              <WarningIcon fontSize="medium" sx={{ color: '#54595E99' }} />
              <Typography fontSize={'14px'} color={'#54595E99'}>
                You don't have selected Billboard Deal. To select Deal, please
                click on the Button and choose offer!
              </Typography>
            </Box>
          )}
        </Box>
        {billboardDeal == undefined || billboardDeal == null ? (
          <MarketButton variant="outlined" onClick={() => setOpen(true)}>
            Choose Offer
          </MarketButton>
        ) : (
          <></>
        )}
      </Box>
    </EconomyCard>
  );
};

export default BillboardCard;
