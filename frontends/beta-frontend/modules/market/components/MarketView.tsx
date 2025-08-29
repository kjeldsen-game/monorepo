import { Alert, Box, useMediaQuery } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import { theme } from '@/libs/material/theme';
import MarketFilter from './filter/MarketFilter';
import AuctionDialog from './dialog/AuctionDialog';
import { MarketColumns } from './columns/MarketColumns';
import DashboardLink from '@/shared/components/DashboardLink';
import { AuctionResponse } from '../types/responses';
import Grid from '@/shared/components/Grid/Grid';

interface MarketProps {
  auctions: AuctionResponse[] | undefined;
  setFilter: (filter: string) => void;
  setAuction: (auctionId: string) => void;
}

const MarketView: React.FC<MarketProps> = ({
  auctions,
  setFilter,
  setAuction,
}: MarketProps) => {
  const [activeAuction, setActiveAuction] = useState<AuctionResponse | undefined>(
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

  const handleRowButtonClick = (auction: AuctionResponse) => {
    setActiveAuction(auction);
    setAuction(auction.id);
    setOpen(true);
  };

  const isXs = useMediaQuery(theme.breakpoints.down('sm'));
  const memoizedColumns = useMemo(() => MarketColumns(handleRowButtonClick, isXs), [isXs]);

  return (
    <>
      {/* <DashboardLink children={'Back to Dashboard'} /> */}

      <Alert sx={{ mb: '16px', borderLeft: '8px solid #EF7B2B' }} severity="warning">
        Please note: the app is not continuously deployed, so auction end times may behave unexpectedly.
        That said, the bidding system is fully functional and ready for beta testing — you can place bids on players successfully.
      </Alert>

      <Box sx={{ width: '100%', background: 'white' }} padding={2} borderRadius={2} boxShadow={1}>

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
          disableColumnMenu={true}
          getRowId={(row) => row.id}
          rows={auctions}
          columns={memoizedColumns}
        />
      </Box >
    </>

  );
};

export default MarketView;
