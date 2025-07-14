import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import ColHeader from '../common/components/ColHeader';
import { getColumnConfig } from '../common/config/ColumnsConfig';
import { TransferColumn } from './common/TransferColumn';

export const economyColumns = () => {
  const columns: GridColDef[] = [
    {
      ...getColumnConfig('left'),
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
    TransferColumn((row) => row, 'center', 'This Week', 'thisWeekAmount'),
    TransferColumn(
      (row) => row, 'right', 'This Season', 'thisSeasonAmount'
    ),
  ];

  return columns;
};
