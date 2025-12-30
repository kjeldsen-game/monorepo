import { theme } from '@/libs/material/theme';
import { useModalManager } from '@/shared/hooks/useModalManager';
import { Box, useMediaQuery } from '@mui/material';
import { useMarketApi } from 'modules/market/hooks/useMarketApi';
import { AuctionResponse } from 'modules/market/types/responses';
import React, { useEffect, useMemo, useState } from 'react'
import { MarketColumns } from '../columns/MarketColumns';
import AuctionDialog from '../dialog/AuctionDialog';
import MarketFilter from '../filter/MarketFilter';
import Grid from '@/shared/components/Grid/Grid';
import NoDataError from '@/shared/components/Grid/no-data-error/NoDataError';
import { GavelOutlined } from '@mui/icons-material';

const MarketTab = () => {

  const [filter, setFilter] = useState<string>('');
  const [auction, setAuction] = useState<string | undefined>(undefined);

  const { data: auctions, isLoading } = useMarketApi(filter)
  console.log(auctions)

  const { setOpen, handleCloseModal, open } = useModalManager();
  const [activeAuction, setActiveAuction] = useState<AuctionResponse | undefined>(
    undefined,
  );

  const handleRowButtonClick = (auction: AuctionResponse) => {
    setActiveAuction(auction);
    setAuction(auction.id);
    setOpen(true);
  };

  const isXs = useMediaQuery(theme.breakpoints.down('sm'));
  const memoizedColumns = useMemo(() => MarketColumns(isXs, false, handleRowButtonClick,), [isXs]);


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

  return (
    <>
      <AuctionDialog
        auction={activeAuction}
        open={open}
        handleClose={handleCloseModal}
      />
      <MarketFilter setFilter={setFilter} />
      <Grid
        sx={{ minHeight: 300 }}
        loading={isLoading || !auctions}
        disableColumnMenu={true}
        getRowId={(row) => row.id}
        rows={auctions || []}
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
    </>
  )
}

export default MarketTab