import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import ColHeader from '../components/ColHeader';
import { getColumnConfig } from '../config/ColumnsConfig';
import { Box } from '@mui/material';

export function BaseTextColumn<T>(
    header: string,
    getValue: (row: T) => any,
    alignment: GridAlignment = 'center',
    minWidth?: number
): GridColDef {
    return {
        ...getColumnConfig(alignment),
        field: header,
        minWidth: minWidth,
        renderHeader: () => <ColHeader header={header} align={alignment} />,
        renderCell: (params: GridCellParams<T>) => {
            const justifyContent =
                alignment === 'left' ? 'flex-start' :
                    alignment === 'right' ? 'flex-end' :
                        'center';

            const paddingStyle =
                alignment === 'left'
                    ? { paddingLeft: '10px' }
                    : alignment === 'right'
                        ? { paddingRight: '10px' }
                        : {};
            const value = getValue(params.row);
            return (
                <Box sx={{ ...paddingStyle }} display={'flex'} height={'100%'}
                    justifyContent={justifyContent} alignItems={'center'} >
                    {value}
                </Box >
            );
        },
    };
}
