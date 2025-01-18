import {
  PlayerPosition,
  PlayerPositionColor,
  PlayerPositionColorNew,
} from '@/shared/models/PlayerPosition';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import LocalAtmIcon from '@mui/icons-material/LocalAtm';
import { isNegative } from '@/shared/utils/EconomyUtils';
import { playerCommonColumns } from './PlayerCommonColumns';

export const playerTransactionsColumns = () => {
  const columns: GridColDef[] = [
    ...playerCommonColumns(true, false),
    {
      field: 'thisWeek',
      renderHeader: () => <div>This Week</div>,
      headerAlign: 'center' as GridAlignment,
      align: 'right' as GridAlignment,
      minWidth: 70,
      flex: 1,
      renderCell: (params: GridCellParams) => (
        <Box
          sx={{
            paddingRight: '20px',
            color: isNegative(params.row.transactionSummary.weekSummary)
              ? '#C51A1A'
              : 'black',
          }}>
          {params.row.transactionSummary.weekSummary.toLocaleString(undefined, {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
          })}{' '}
          $
        </Box>
      ),
    },
    {
      field: 'thisSeason',
      renderHeader: () => <div>This Season</div>,
      headerAlign: 'center' as GridAlignment,
      align: 'right' as GridAlignment,
      minWidth: 70,
      flex: 1,
      renderCell: (params: GridCellParams) => (
        <Box
          sx={{
            paddingRight: '20px',
            color: isNegative(params.row.transactionSummary.seasonSummary)
              ? '#C51A1A'
              : 'black',
          }}>
          {' '}
          {params.row.transactionSummary.seasonSummary.toLocaleString(
            undefined,
            {
              minimumFractionDigits: 2,
              maximumFractionDigits: 2,
            },
          )}{' '}
          $
        </Box>
      ),
    },
  ];

  return columns;
};
