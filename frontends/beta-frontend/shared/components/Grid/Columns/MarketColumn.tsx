import {
  GridCellParams,
  GridColDef,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
  PlayerPosition,
  PlayerPositionColorNew,
} from '@/shared/models/PlayerPosition';
import MarketButton from '../../Market/MarketButton';
import { AuctionMarket } from '@/shared/models/Auction';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { playerSkillsColumns } from './PlayerSkillsColumns';
import { playerCommonColumns } from './PlayerCommonColumns';
import { baseColumnConfig } from './ColumnsConfig';

export const marketColumn = (
  handleButtonClick: (auction: AuctionMarket) => void,
) => {
  const columns: GridColDef[] = [
    ...playerCommonColumns(true, false),
    {
      ...baseColumnConfig,
      field: 'averageBid',
      renderHeader: () => <div>Average Bid</div>,
      minWidth: 70,
      renderCell: (params) => (
        <div style={{ fontWeight: 'bold' }}>{params.row.averageBid} $</div>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'myBid',
      renderHeader: () => <div>My Bid</div>,
      minWidth: 70,
      renderCell: (params) => (
        <div style={{ fontWeight: 'bold' }}>{params.row.bid || 0} $</div>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'totalBids',
      renderHeader: () => <div>Total bidders</div>,
      minWidth: 70,
      valueGetter: (params) => params.row.bidders,
    },
    {
      ...baseColumnConfig,
      field: 'action',
      renderHeader: () => <div>Action</div>,
      sortable: false,
      minWidth: 50,
      renderCell: (params: GridCellParams) => (
        <>
          <MarketButton
            sx={{ height: '34px', minWidth: '34px' }}
            children={'$'}
            onClick={() => handleButtonClick(params.row)}
          />
        </>
      ),
    },
  ];

  return columns;
};
