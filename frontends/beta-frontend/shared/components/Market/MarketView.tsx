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
}

const MarketView: React.FC<MarketProps> = ({
  auctions,
  refetch,
}: MarketProps) => {
  const [activeAuction, setActiveAuction] = useState<AuctionMarket | undefined>(
    undefined,
  );
  const [open, setOpen] = useState<boolean>(false);

  const handleRowButtonClick = (auction: AuctionMarket) => {
    setActiveAuction(auction);
    setOpen(true);
  };

  useEffect(() => {
    if (activeAuction !== undefined && auctions?.length > 0) {
      const updatedAuction = auctions?.find(
        (auction) => auction.id === activeAuction.id,
      );

      if (updatedAuction) {
        setActiveAuction(updatedAuction);
      }
    }
  }, [activeAuction, auctions]);

  const memoizedColumns = useMemo(() => marketColumn(handleRowButtonClick), []);

  const handleCloseModal = () => {
    setOpen(false);
  };

  return (
    <Box sx={{ width: '100%' }}>
      <DashboardLink children={'Back to Dashboard'} />
      <MarketModal
        auction={activeAuction}
        refetch={refetch}
        open={open}
        handleClose={handleCloseModal}
      />
      <Box
        sx={{
          marginBottom: '2rem',
          alignItems: 'center',
        }}>
        <MarketFilter refetch={refetch} />
      </Box>
      <Box sx={{ width: '100%' }}>
        {auctions ? (
          <Grid
            sx={{
              '& .MuiDataGrid-columnSeparator': {
                display: 'none',
              },
              '& .MuiDataGrid-columnHeaders': {
                padding: 0, // Removes padding from the column header container
              },
              '& .MuiDataGrid-columnHeader': {
                padding: 0, // Removes padding from each individual header cell
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
