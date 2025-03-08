import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import { isNegative } from '@/shared/utils/EconomyUtils';
import { baseColumnConfig } from './ColumnsConfig';

export const economyColumns = () => {
  const columns: GridColDef[] = [
    {
      field: 'concept',
      renderHeader: () => <div>Concept</div>,
      headerAlign: 'center' as GridAlignment,
      minWidth: 130,
      flex: 1,
      renderCell: (params: GridCellParams) => (
        <Box
          display={'flex'}
          alignItems={'center'}
          paddingX={
            params.row.context !== 'Total Income' &&
            params.row.context !== 'Total Outcome' &&
            params.row.context !== 'Total Balance'
              ? '40px'
              : '0px'
          }>
          {params.row.context.includes('Total')
            ? params.row.context
            : convertSnakeCaseToTitleCase(params.row.context)}
        </Box>
      ),
    },
    {
      field: 'thisWeek',
      renderHeader: () => <div>This Week</div>,
      ...baseColumnConfig,
      minWidth: 70,
      renderCell: (params: GridCellParams) => (
        <Box
          sx={{
            paddingRight: '20px',
            color: isNegative(params.row.thisWeekAmount) ? '#C51A1A' : 'black',
          }}>
          {params.row.thisWeekAmount.toLocaleString(undefined, {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
          })}{' '}
          $
        </Box>
      ),
    },
    {
      field: 'thisSeason',
      ...baseColumnConfig,
      renderHeader: () => <div>This Season</div>,
      minWidth: 70,
      renderCell: (params: GridCellParams) => (
        <Box
          sx={{
            paddingRight: '20px',
            color: isNegative(params.row.thisSeasonAmount)
              ? '#C51A1A'
              : 'black',
          }}>
          {params.row.thisSeasonAmount.toLocaleString(undefined, {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
          })}{' '}
          $
        </Box>
      ),
    },
  ];

  return columns;
};
