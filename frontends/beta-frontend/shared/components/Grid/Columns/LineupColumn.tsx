import { GridColDef, GridValueGetterParams } from '@mui/x-data-grid';
import { baseColumnConfig } from './ColumnsConfig';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { playerCommonColumns } from './PlayerCommonColumns';

export const lineupColumn = () => {
  const columns: GridColDef[] = [
    ...playerCommonColumns(true, false),
    {
      ...baseColumnConfig,
      field: 'playerOrder',
      renderHeader: () => <div>Player Order</div>,
      valueGetter: (params: GridValueGetterParams) =>
        convertSnakeCaseToTitleCase(params.row.playerOrder),
      minWidth: 50,
      flex: 1,
    },
  ];

  return columns;
};
