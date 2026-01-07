import CustomButton from '@/shared/components/Common/CustomButton'
import CustomIconButton from '@/shared/components/Common/CustomIconButton'
import { Box, Card, Grow, IconButton, Typography } from '@mui/material'
import React from 'react'
import LocalAtmOutlined from '@mui/icons-material/LocalAtmOutlined';
import { useMarketApi } from 'modules/market/hooks/useMarketApi';
import AuctionDialog from 'modules/market/components/dialog/AuctionDialog';
import { useModalManager } from '@/shared/hooks/useModalManager';

interface PlayerOnSaleNotificationProps {
    playerId: string;
}

const PlayerOnSaleNotification: React.FC<PlayerOnSaleNotificationProps> = ({ playerId }) => {

    console.log(playerId)

    const { open, setOpen, handleCloseModal } = useModalManager();
    const { data } = useMarketApi(`playerId=${playerId}`)

    return (
        <Grow in={data?.length > 0}>
            <Card

                sx={{

                    borderRadius: '4px',
                    width: '100%',
                    height: '50px',
                    background: '#FF3F84',
                    marginBottom: '1rem',
                    color: '#FFFFFF',
                    display: data?.length > 0 ? 'flex' : 'none',
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
                </Box>

                <AuctionDialog
                    auction={data?.[0]}
                    open={open}
                    handleClose={handleCloseModal}
                />

                <CustomIconButton
                    sx={{ mr: 1 }}
                    variant='outlined'
                    onClick={() => setOpen(true)}>
                    <LocalAtmOutlined sx={{ marginX: '5px' }} />
                </CustomIconButton>
            </Card>
        </Grow >
    )
}

export default PlayerOnSaleNotification