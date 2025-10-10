import { Box, useMediaQuery } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import { theme } from '@/libs/material/theme';
import MarketFilter from './filter/MarketFilter';
import AuctionDialog from './dialog/AuctionDialog';
import { MarketColumns } from './columns/MarketColumns';
import { AuctionResponse } from '../types/responses';
import Grid from '@/shared/components/Grid/Grid';
import NoDataError from '@/shared/components/Grid/no-data-error/NoDataError';
import { GavelOutlined } from '@mui/icons-material';

interface MarketProps {
  loading: boolean;
  auctions: AuctionResponse[] | undefined;
  setFilter: (filter: string) => void;
  setAuction: (auctionId: string) => void;
}

const MarketView: React.FC<MarketProps> = ({
  auctions,
  setFilter,
  setAuction,
  loading
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
      <Box sx={{ width: '100%', background: 'white' }} padding={1} borderRadius={2} boxShadow={1}>

        <AuctionDialog
          auction={activeAuction}
          open={open}
          handleClose={handleCloseModal}
        />
        <MarketFilter setFilter={setFilter} />
        <Grid
          loading={loading || !auctions}
          disableColumnMenu={true}
          getRowId={(row) => row.id}
          rows={auctions}
          columns={memoizedColumns}
          slots={{
            noRowsOverlay: () => (
              <NoDataError
                icon={GavelOutlined}
                title="No Active Auctions"
                subtitle="There are currently no active auctions."
              />
            )
          }}
        />
      </Box >
    </>

  );
};

export default MarketView;
