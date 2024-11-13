import * as React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import LocalAtmIcon from '@mui/icons-material/LocalAtm';
import { IconButton, TextField } from '@mui/material';
import { useSession } from 'next-auth/react';
import MarketButton from './MarketButton';
import AuctionDetailData from './AuctionDetailData';
import CustomModal from '../CustomModal';
import VerifiedIcon from '@mui/icons-material/Verified';
import { CloseOutlined } from '@mui/icons-material';
import { AuctionMarket } from '@/shared/models/Auction';

interface AuctionProps {
  auction: AuctionMarket | undefined;
  refetch: () => void;
  update: (auctionId: number) => void;
  open: boolean;
  handleClose: () => void;
}

const MarketModal: React.FC<AuctionProps> = ({
  auction,
  refetch,
  update,
  open,
  handleClose,
}: AuctionProps) => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const [bid, setBid] = React.useState<number>(0);
  const [confirmation, setConfirmation] = React.useState(false);
  const [updateError, setUpdateError] = React.useState<string | null>(null);

  const handleCloseModal = () => {
    handleClose();
    setUpdateError(null);
    setConfirmation(false);
    setBid(0);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setBid(Number(e.target.value));
  };

  const handleConfirmationButtonClick = () => {
    setUpdateError(null);
    setConfirmation(true);
  };

  const handleButtonClick = async () => {
    try {
      const response = await update(bid);
      if (response.status == 500) {
        setConfirmation(false);
        setUpdateError(response.message);
      } else {
        setUpdateError('Success');
        setBid(0);
        refetch();
      }
    } catch (error) {
      console.error('Failed to update auction:', error);
    }
  };

  function formatDateToDDMMYY(dateString?: string): string {
    if (!dateString) {
      return '';
    }
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = String(date.getFullYear()).slice(-2);

    return `${day}/${month}/${year}`;
  }

  return (
    <>
      <CustomModal
        sx={{ width: '400px' }}
        onClose={handleCloseModal}
        open={open}>
        <Box>
          <IconButton
            sx={{
              width: '24px',
              height: '24px',
              position: 'absolute',
              left: '90%',
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
          {updateError == 'Success' ? (
            <VerifiedIcon fontSize="large" sx={{ color: '#FF3F84' }} />
          ) : (
            <LocalAtmIcon fontSize="large" sx={{ color: '#FF3F84' }} />
          )}
        </Box>
        <Typography
          variant="h6"
          component="h2"
          sx={{ fontSize: '20px' }}
          textAlign={'center'}>
          {updateError == 'Success' ? (
            <>
              Congratulations
              <br />
              <Typography sx={{ fontSize: '16px' }}>
                Your bid has beed placed
              </Typography>
            </>
          ) : (
            'Bid for a player'
          )}
        </Typography>
        <Typography
          variant="body2"
          sx={{
            color: '#54595E99',
            padding: '8px',
            borderRadius: '4px',
            textAlign: 'center',
          }}>
          Lorem ipsum que tu quieras para explicar como es el proceso de compra
          de un jugador.
        </Typography>

        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            width: '100%',
          }}>
          <AuctionDetailData
            title="Average Bid"
            value={`${auction?.averageBid} $`}
          />
          <AuctionDetailData title="Total Bids" value={`${auction?.bidders}`} />
          <AuctionDetailData
            title="Auction Date"
            value={`${formatDateToDDMMYY(auction?.endedAt)}`}
          />
          <AuctionDetailData
            title="My bid"
            value={`${auction?.bid}$` || '0$'}
          />
        </Box>
        {updateError === 'Success' ? (
          <></>
        ) : (
          <>
            {userData?.user.teamId === auction?.teamId ? (
              <Typography
                sx={{
                  color: '#FF3F84',
                  padding: '8px',
                  borderRadius: '4px',
                  textAlign: 'center',
                }}
                variant="body2">
                You are the owner of this player
              </Typography>
            ) : (
              <>
                {!confirmation ? (
                  <TextField
                    type="number"
                    placeholder="Your bid here..."
                    onChange={handleInputChange}
                    value={bid || ''}
                    sx={{
                      mt: 2,
                      mb: 2,
                      width: '100%',
                      '& .MuiOutlinedInput-root': {
                        '& fieldset': {
                          borderColor: '#c4c4c4',
                        },
                        '&:hover fieldset': {
                          borderColor: '#FF3F84',
                        },
                        '&.Mui-focused fieldset': {
                          borderColor: '#FF3F84',
                        },
                      },
                      '& input[type=number]': {
                        MozAppearance: 'textfield',
                      },
                      '& input[type=number]::-webkit-outer-spin-button, & input[type=number]::-webkit-inner-spin-button':
                        {
                          WebkitAppearance: 'none',
                          margin: 0,
                        },
                    }}
                  />
                ) : (
                  <AuctionDetailData
                    title="Your Bid"
                    value={`${bid} $`}
                    sx={{
                      color: '#FF3F84',
                      fontSize: '30px',
                      fontWeight: '900',
                    }}
                  />
                )}

                {updateError && (
                  <div
                    style={{
                      background: '#FFE5E5',
                      width: '100%',
                      padding: '10px 20px',
                    }}>
                    {updateError}
                  </div>
                )}

                <MarketButton
                  sx={{ mt: 2, width: '100%' }}
                  onClick={
                    !confirmation
                      ? handleConfirmationButtonClick
                      : handleButtonClick
                  }>
                  {!confirmation ? 'PLACE MY BID' : 'YES, I AM SURE'}
                </MarketButton>
              </>
            )}
          </>
        )}
      </CustomModal>
    </>
  );
};

export default MarketModal;
