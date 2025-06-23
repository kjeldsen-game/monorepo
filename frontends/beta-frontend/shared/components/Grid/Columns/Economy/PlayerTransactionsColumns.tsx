import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { isNegative } from '@/shared/utils/EconomyUtils';
import { baseColumnConfig, leftColumnConfig, rightColumnConfig } from '../ColumnsConfig';
import ColHeader from '../Common/ColHeader';
import ColLink from '../Common/ColLink';


export const playerTransactionsColumns = () => {
  const columns: GridColDef[] = [
    {
      ...leftColumnConfig,
      field: 'name',
      minWidth: 80,
      renderHeader: () => <ColHeader header={'Name'} align={'left'} />,
      renderCell: (params: GridCellParams) => {
        // console.log(params.row.player.name);
        return (
          <ColLink urlValue={`/player/${params.row.player.id}`}>
            {params.row.player.name}
          </ColLink>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'thisWeek',
      renderHeader: () => <ColHeader header='This Week' />,
      minWidth: 70,
      renderCell: (params: GridCellParams) => (
        <Box
          sx={{
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
      ...rightColumnConfig,
      field: 'thisSeason',
      renderHeader: () => <ColHeader align='right' header='This Season' />,
      minWidth: 70,
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
