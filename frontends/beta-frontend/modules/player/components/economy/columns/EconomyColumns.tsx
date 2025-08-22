import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { TransferColumn } from './common/TransferColumn';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { Transaction } from 'modules/player/types/Economy';

export const EconomyColumns = () => {
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
              ? '10px'
              : '5px'
          }>
          {params.row.context.includes('Total')
            ? params.row.context
            : convertSnakeCaseToTitleCase(params.row.context)}
        </Box>
      ),
    },
    TransferColumn((row: Transaction) => row, 'center', 'This Week', 'thisWeekAmount'),
    TransferColumn(
      (row: Transaction) => row, 'right', 'This Season', 'thisSeasonAmount'
    ),
  ];

  return columns;
};
