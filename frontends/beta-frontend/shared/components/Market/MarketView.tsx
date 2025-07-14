import { Alert, Box, useMediaQuery } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import { marketColumn } from '../Grid/Columns/MarketColumn';
import Grid from '../Grid/Grid';
import DashboardLink from '../DashboardLink';
import { AuctionMarket } from '@/shared/models/market/Auction';
import { theme } from '@/libs/material/theme';
import MarketFilter from './filter/MarketFilter';
import AuctionDialog from './dialog/AuctionDialog';

interface MarketProps {
  auctions: AuctionMarket[] | undefined;
  setFilter: (filter: string) => void;
  setAuction: (auctionId: string) => void;
}

const MarketView: React.FC<MarketProps> = ({
  auctions,
  setFilter,
  setAuction,
}: MarketProps) => {
  const [activeAuction, setActiveAuction] = useState<AuctionMarket | undefined>(
    undefined,
  );
  const [open, setOpen] = useState<boolean>(false);

  useEffect(() => {
    if (activeAuction !== undefined && auctions && auctions?.length > 0) {
      const updatedAuction = auctions?.find(
        (auction) => auction.id === activeAuction.id,
      );
      if (updatedAuction) {
        setActiveAuction(updatedAuction);
      }
    }
  }, [activeAuction, auctions]);

  const handleCloseModal = () => {
    setOpen(false);
  };

  const handleRowButtonClick = (auction: AuctionMarket) => {
    setActiveAuction(auction);
    setAuction(auction.id);
    setOpen(true);
  };

  const isXs = useMediaQuery(theme.breakpoints.down('sm'));
  const memoizedColumns = useMemo(() => marketColumn(handleRowButtonClick, isXs), [isXs]);

  return (
    <Box sx={{ width: '100%' }}>
      <DashboardLink children={'Back to Dashboard'} />

      <Alert sx={{ mb: '16px' }} severity="warning">
        Please note: the app is not continuously deployed, so auction end times may behave unexpectedly.
        That said, the bidding system is fully functional and ready for beta testing — you can place bids on players successfully.
      </Alert>

      <AuctionDialog
        auction={activeAuction}
        open={open}
        handleClose={handleCloseModal}
      />
      <Box
        sx={{
          marginBottom: '2rem',
          alignItems: 'center',
        }}>
        <MarketFilter setFilter={setFilter} />
      </Box>
      <Grid
        loading={auctions === undefined || null}
        sx={{
          '& .MuiDataGrid-columnHeaders': {
            padding: 0,
          },
          '& .MuiDataGrid-columnHeader': {
            padding: 0,
          },
          '& .MuiDataGrid-cell': {
            padding: '0px !important',
          },
        }}
        disableColumnMenu={true}
        getRowId={(row) => row.id}
        rows={auctions}
        columns={memoizedColumns}
      />
    </Box>
  );
};

export default MarketView;
