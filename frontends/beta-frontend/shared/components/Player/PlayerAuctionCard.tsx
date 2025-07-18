import { useState } from 'react';
import { Box, Grow, Typography } from '@mui/material';
import LocalAtmOutlined from '@mui/icons-material/LocalAtmOutlined';
import MarketButton from '../Market/MarketButton';
import { useSession } from 'next-auth/react';

interface PlayerAuctionCardProps {
  auction: any;
}

const PlayerAuctionCard = ({ auction }: PlayerAuctionCardProps) => {
  const { data } = useSession();

  const [open, setOpen] = useState<boolean>(false);

  const handleOpenModal = (state: boolean) => {
    setOpen(state);
  };

  if (auction === undefined || auction.length === 0) {
    return null;
  }
  return (
    <Grow in={auction.length > 0}>
      <Box
        sx={{
          borderRadius: '4px',
          width: '100%',
          height: '50px',
          background: '#FF3F84',
          marginBottom: '1rem',
          color: '#FFFFFF',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          paddingY: '30px',
        }}>
        <Box display="flex">
          <Box
            sx={{
              display: 'flex',
              alignItems: 'center',
              paddingX: '20px',
            }}>
            <LocalAtmOutlined />
            <Typography fontWeight="700" fontSize="15px" paddingLeft="10px">
              THIS PLAYER IS FOR SALE
            </Typography>
          </Box>

          <Box
            sx={{
              paddingX: '20px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}>
            <Typography sx={{ opacity: 0.7 }}>Average Bid:</Typography>
            <span
              style={{
                fontWeight: '700',
                paddingLeft: '5px',
              }}>
              {auction[0].averageBid}$
            </span>
          </Box>

          <Box
            sx={{
              paddingX: '20px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}>
            <Typography sx={{ opacity: 0.7 }}>Bids:</Typography>
            <span
              style={{
                fontWeight: '700',
                paddingLeft: '5px',
              }}>
              {auction[0].bidders}
            </span>
          </Box>
        </Box>

        {/* <MarketModal
          auction={auction[0]}
          open={open}
          handleClose={() => handleOpenModal(false)}
        /> */}

        <MarketButton
          sx={{
            fontWeight: '700',
            marginRight: '10px',
            '&:hover': {
              backgroundColor: '#FFFFFF',
              color: '#FF3F84',
              borderColor: 'transparent',
            },
          }}
          onClick={() => handleOpenModal(true)}>
          <LocalAtmOutlined sx={{ marginX: '5px' }} />
          BID FOR THIS PLAYER
        </MarketButton>
      </Box>
    </Grow>
  );
};

export default PlayerAuctionCard;
