import { GridColDef } from '@mui/x-data-grid';
import { BaseTextColumn } from '@/shared/components/Grid/Columns/common/columns/BaseTextColumn';

export const PlayerColumns = (isXs: boolean) => {
    const columns: GridColDef[] = [
        BaseTextColumn<any>('', (row) => row.col1, 'left'),
        BaseTextColumn<any>('GP', (row) => row.col2, 'center'),
        BaseTextColumn<any>('Goals', (row) => row.col3, 'center'),
        BaseTextColumn<any>('Assists', (row) => row.col4, 'center'),
        BaseTextColumn<any>('Tackles', (row) => row.col5, 'center'),
        BaseTextColumn<any>('Crd', (row) => row.col6, 'center'),
        BaseTextColumn<any>('Man of Match', (row) => row.col7, 'center'),
        BaseTextColumn<any>('Rating', (row) => row.col8, 'right')
    ];
    return columns;
};
