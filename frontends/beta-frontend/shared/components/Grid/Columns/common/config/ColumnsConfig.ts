import { GridAlignment } from '@mui/x-data-grid';

type ColumnConfig = {
  hideSortIcons: boolean;
  headerAlign: GridAlignment;
  align: GridAlignment;
  flex: number;
};

const baseColumnConfig: Omit<ColumnConfig, 'headerAlign' | 'align'> &
  Partial<Pick<ColumnConfig, 'headerAlign' | 'align'>> = {
  hideSortIcons: true,
  flex: 0.5,
};

export function getColumnConfig(
  alignment: GridAlignment = 'center',
): ColumnConfig {
  return {
    ...baseColumnConfig,
    headerAlign: alignment,
    align: alignment,
  };
}
