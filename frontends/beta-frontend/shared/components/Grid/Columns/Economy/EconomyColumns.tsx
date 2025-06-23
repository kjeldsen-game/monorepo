import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import { isNegative } from '@/shared/utils/EconomyUtils';
import { baseColumnConfig, leftColumnConfig, rightColumnConfig } from '../ColumnsConfig';
import ColHeader from '../Common/ColHeader';

export const economyColumns = () => {
  const columns: GridColDef[] = [
    {
      ...leftColumnConfig,
      field: 'concept',
      renderHeader: () => <ColHeader header={'Concept'} align={'left'} />,
      minWidth: 130,
      renderCell: (params: GridCellParams) => (
        <Box
          display={'flex'}
          alignItems={'center'}
          paddingX={
            params.row.context !== 'Total Income' &&
              params.row.context !== 'Total Outcome' &&
              params.row.context !== 'Total Balance'
              ? '20px'
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
      renderHeader: () => <ColHeader header='This Week' />,
      ...baseColumnConfig,
      minWidth: 70,
      renderCell: (params: GridCellParams) => (
        <Box
          sx={{
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
      ...rightColumnConfig,
      renderHeader: () => <ColHeader align='right' header='This Season' />,
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
