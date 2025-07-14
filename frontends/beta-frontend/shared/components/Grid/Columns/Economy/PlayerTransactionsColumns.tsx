import { GridColDef } from '@mui/x-data-grid';
import { PlayerNameColumn } from '../common/columns/PlayerNameColumn';
import { TransferColumn } from './common/TransferColumn';


export const playerTransactionsColumns = () => {
  const columns: GridColDef[] = [
    PlayerNameColumn((row) => row.player),
    TransferColumn(
      (row) => row.transactionSummary, 'center', 'This Week', 'weekSummary'
    ),
    TransferColumn((row) => row.transactionSummary, 'right', 'This Season', 'seasonSummary'),
  ];

  return columns;
};
