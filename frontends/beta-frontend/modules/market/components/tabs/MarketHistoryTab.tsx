import Grid from '@/shared/components/Grid/Grid'
import NoDataError from '@/shared/components/Grid/no-data-error/NoDataError'
import { GavelOutlined } from '@mui/icons-material'
import { useAuctionHistoryApi } from 'modules/market/hooks/useAuctionHistoryApi'
import React, { useMemo, useRef, useState } from 'react'
import { MarketHistoryColumns } from '../columns/MarketHistoryColumns'
import { useMediaQuery } from '@mui/material'
import { theme } from '@/libs/material/theme'
import HistoryDialog from '../dialog/HistoryDialog'
import { useModalManager } from '@/shared/hooks/useModalManager'
import { set } from 'react-hook-form'
import { useSession } from 'next-auth/react'

const MarketHistoryTab = () => {
  const {data: userData} = useSession();
  const isXs = useMediaQuery(theme.breakpoints.down('sm'));
  const { open, handleCloseModal, setOpen } = useModalManager()

  const [auction, setAuction] = useState<any | undefined>(undefined);
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 5,
  });

  const { data, isLoading, error } = useAuctionHistoryApi(paginationModel.pageSize, paginationModel.page);

  const handleActionButtonClick = (auction: any) => {
    setAuction(auction);
    setOpen(true);
  }

  const memoizedColumns = useMemo(() => MarketHistoryColumns(handleActionButtonClick, userData?.user?.teamId), [userData?.user?.teamId]);

  const rowCountRef = useRef(data?.totalElements || 0);

  const rowCount = useMemo(() => {
    if (data?.totalElements !== undefined) {
      rowCountRef.current = data.totalElements;
    }
    return rowCountRef.current;
  }, [data?.totalElements]);


  return (
    <>
      <HistoryDialog
        auction={auction}
        open={open}
        handleClose={handleCloseModal}
      />
      <Grid
        loading={isLoading}
        disableColumnMenu={true}
        getRowId={(row) => row.id}
        rowCount={rowCount}
        paginationMode='server'
        pagination={true}
        paginationModel={paginationModel}
        onPaginationModelChange={setPaginationModel}
        rows={data?.content || []}
        columns={memoizedColumns}
        slots={{
          noRowsOverlay: () => (
            <NoDataError
              icon={GavelOutlined}
              title="No Auction History"
              subtitle="There are currently no active auctions."
            />
          )
        }}
      />
    </>
  )
}

export default MarketHistoryTab