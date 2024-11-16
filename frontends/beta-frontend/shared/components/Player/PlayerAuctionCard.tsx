import { useEffect, useState } from 'react';
import { Box, Grow, Typography } from '@mui/material';
import LocalAtmOutlined from '@mui/icons-material/LocalAtmOutlined';
import MarketModal from '../Market/MarketModal';
import MarketButton from '../Market/MarketButton';
import { useRouter } from 'next/router';
import { useSession } from 'next-auth/react';
import { useAuctionRepository } from '@/pages/api/market/useAuctionRepository';

interface PlayerAuctionCardProps {
  refreshTrigger: boolean;
}

const PlayerAuctionCard = ({ refreshTrigger }: PlayerAuctionCardProps) => {
  const { data } = useSession();

  const { auctions: auction } = useAuctionRepository(
    '',
    data?.accessToken,
    `playerId=${useRouter().query.id}`,
  );

  console.log(auction);

  const [open, setOpen] = useState<boolean>(false);

  // useEffect(() => {
  //   refetch();
  // }, [refreshTrigger]);

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

        <MarketModal
          auction={auction[0]}
          open={open}
          handleClose={() => handleOpenModal(false)}
        />

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
