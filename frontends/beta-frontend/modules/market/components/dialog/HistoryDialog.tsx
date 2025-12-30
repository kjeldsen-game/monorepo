import CloseButton from '@/shared/components/Common/CloseButton'
import { Box, Dialog, DialogContent, DialogTitle, Typography, useMediaQuery } from '@mui/material'
import React, { useMemo } from 'react'
import { AuctionBidLogColumns } from '../columns/AuctionBidLogColumns';
import Grid from '@/shared/components/Grid/Grid';
import { MarketColumns } from '../columns/MarketColumns';
import { theme } from '@/libs/material/theme';
import { useSession } from 'next-auth/react';
import LiveBidFeed from './LiveBidFeed';
import { DataGrid } from '@mui/x-data-grid';

interface HistoryDialogProps {
  open: boolean;
  handleClose: () => void;
  auction?: any | undefined;
}

const HistoryDialog: React.FC<HistoryDialogProps> = ({ open, handleClose, auction }) => {
  const { data: userData } = useSession();
  const isXs = useMediaQuery(theme.breakpoints.down('sm'));

  const memoizedPlayerColumns = useMemo(() => MarketColumns(isXs, true), [isXs]);
  const memoizedBidsColumns = useMemo(() => AuctionBidLogColumns(), []);

  console.log('auction', auction);

  return (
    <Dialog open={open} onClose={handleClose} scroll={'body'} fullWidth maxWidth="lg">
      <CloseButton handleCloseModal={handleClose} />
      <DialogTitle display={'flex'} flexDirection={'column'} alignItems={'center'} justifyItems={'center'}>
        <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
          Auction Details
        </Typography>
      </DialogTitle>
      <DialogContent>
        <Box >
          <Grid
            // sx={{padding: 2, background: '#f3f4f6'}}
            loading={!auction}
            hideFooter={true}
            disableColumnMenu={true}
            getRowId={(row) => row.id}
            rows={auction ? [auction] : []}
            columns={memoizedPlayerColumns}
          />
          <Box
            sx={{
              borderRadius: 1,
              background: '#f3f4f6',
              color: theme.palette.quaternary.main,
              padding: 1,
              mt: 2
            }}
          >
            <Typography variant="h6" sx={{ fontWeight: 'bold', pl: 1 }}>
              Bid History
            </Typography>
            <Grid
              sx={{
                '& .MuiDataGrid-row:first-of-type': {
                  display: 'flex',
                  alignItems: 'center',
                  overflow: 'visible', // allow cell border-radius to show
                },
                '& .MuiDataGrid-row:first-of-type .MuiDataGrid-cell': {
                  background: theme.palette.secondary.light,
                  color: theme.palette.secondary.main,
                  border: 'none', // optional, to prevent borders cutting off corners
                },
                '& .MuiDataGrid-row:first-of-type .MuiDataGrid-cell--textLeft': {
                  borderTopLeftRadius: '8px !important',
                  borderBottomLeftRadius: '8px !important',
                },
                '& .MuiDataGrid-row:first-of-type .MuiDataGrid-cell--textRight': {
                  borderTopRightRadius: '8px !important',
                  borderBottomRightRadius: '8px !important',
                },
                '& .MuiDataGrid-cell': {
                  height: '30px',
                },
                '& .MuiDataGrid-row': {
                  display: 'flex',
                  alignItems: 'center',
                  background: '#f3f4f6',
                },
              }}

              loading={!auction}
              hideFooter={true}
              disableColumnMenu={true}
              getRowId={(row) => row.timestamp}
              rows={(auction?.bids || []).slice().reverse()}
              columns={memoizedBidsColumns}
            />
          </Box>
        </Box>
      </DialogContent>
    </Dialog >
  )
}

export default HistoryDialog