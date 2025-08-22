import { GridColDef } from '@mui/x-data-grid';
import { TransferColumn } from './common/TransferColumn';
import { PlayerNameColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerNameColumn';


export const PlayerTransactionsColumns = () => {
  const columns: GridColDef[] = [
    PlayerNameColumn((row) => row.player),
    TransferColumn(
      (row) => row.transactionSummary, 'center', 'This Week', 'weekSummary'
    ),
    TransferColumn((row) => row.transactionSummary, 'right', 'This Season', 'seasonSummary'),
  ];

  return columns;
};
