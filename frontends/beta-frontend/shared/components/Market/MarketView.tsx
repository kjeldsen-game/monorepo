import { Box, CircularProgress } from '@mui/material';
import MarketFilter from './MarketFilter';
import { useEffect, useMemo, useState } from 'react';
import { Auction, AuctionMarket } from '@/shared/models/Auction';
import { marketColumn } from '../Grid/Columns/MarketColumn';
import Grid from '../Grid/Grid';
import DashboardLink from '../DashboardLink';
import MarketModal from './MarketModal';

interface MarketProps {
  auctions: AuctionMarket[] | undefined;
  refetch: () => void;
  updateAuction: (auctionId: number) => void;
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

  const memoizedColumns = useMemo(() => marketColumn(handleRowButtonClick), []);

  return (
    <Box sx={{ width: '100%' }}>
      <DashboardLink children={'Back to Dashboard'} />
      <MarketModal
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
      <Box sx={{ width: '100%' }}>
        {auctions ? (
          <Grid
            sx={{
              '& .MuiDataGrid-columnSeparator': {
                display: 'none',
              },
              '& .MuiDataGrid-columnHeaders': {
                padding: 0,
              },
              '& .MuiDataGrid-columnHeader': {
                padding: 0,
              },
            }}
            disableColumnMenu={true}
            getRowId={(row) => row.id}
            rows={auctions}
            columns={memoizedColumns}
          />
        ) : (
          <CircularProgress />
        )}
      </Box>
    </Box>
  );
};

export default MarketView;
