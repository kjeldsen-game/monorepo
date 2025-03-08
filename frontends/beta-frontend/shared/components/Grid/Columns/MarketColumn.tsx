import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import MarketButton from '../../Market/MarketButton';
import { AuctionMarket } from '@/shared/models/Auction';
import { playerCommonColumns } from './PlayerCommonColumns';
import { baseColumnConfig, rightColumnConfig } from './ColumnsConfig';
import ColHeader from './Common/ColHeader';

export const marketColumn = (
  handleButtonClick: (auction: AuctionMarket) => void,
) => {
  const columns: GridColDef[] = [
    ...playerCommonColumns(true),
    {
      ...baseColumnConfig,
      field: 'averageBid',
      renderHeader: () => <ColHeader header={'Average Bid'} />,
      minWidth: 70,
      renderCell: (params) => (
        <div style={{ fontWeight: 'bold' }}>{params.row.averageBid} $</div>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'myBid',
      renderHeader: () => <ColHeader header={'My Bid'} />,
      minWidth: 70,
      renderCell: (params) => (
        <div style={{ fontWeight: 'bold' }}>{params.row.bid || 0} $</div>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'totalBids',
      renderHeader: () => <ColHeader header={'Total Bidders'} />,
      minWidth: 70,
      valueGetter: (params) => params.row.bidders,
    },
    {
      ...rightColumnConfig,
      field: 'action',
      renderHeader: () => <ColHeader header={'Action'} />,
      minWidth: 50,
      renderCell: (params: GridCellParams) => (
        <>
          <MarketButton
            sx={{ height: '34px', minWidth: '34px', marginRight: '10px' }}
            children={'$'}
            onClick={() => handleButtonClick(params.row)}
          />
        </>
      ),
    },
  ];

  return columns;
};
