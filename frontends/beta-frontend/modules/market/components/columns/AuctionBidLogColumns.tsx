import { GridCellParams, GridColDef } from '@mui/x-data-grid';

import { PriceColumn } from './common/PriceColumn';
import { DateTimeColumn } from '@/shared/components/Grid/Columns/DateTimeColumn';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { Box, Typography } from '@mui/material';

export const AuctionBidLogColumns = (
) => {
  const columns: GridColDef[] = [
    {
      ...getColumnConfig("left"),
      field: "bidder",
      flex: 1.2,
      renderHeader: () => <ColHeader align='left' header={"Bidder"} />,
      renderCell: (params: GridCellParams) => (
        <Typography height={'100%'} display={'flex'} fontWeight={'bold'}
        sx={{justifyContent: 'flex-start', alignItems: 'center', paddingLeft: 1}}>{params.row.team}</Typography>
      ),
    },
    PriceColumn((row) => row.amount, "center", "Amount"),
    DateTimeColumn((row) => row.timestamp, "right", "Time"),
  ];

  return columns;
};
