import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import { getColumnConfig } from '../../common/config/ColumnsConfig';
import ColHeader from '../../common/components/ColHeader';
import { Box } from '@mui/material';
import { isNegative } from '@/shared/utils/EconomyUtils';


export const TransferColumn = (
    getValue: (row: any) => any,
    alignment: GridAlignment = 'center',
    header: string,
    field: string
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: field,
        renderHeader: () => <ColHeader header={header} align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const transfer = getValue(params.row)[field];
            return (
                <Box
                    sx={{
                        paddingRight: alignment === 'right' ? '10px' : '0px',
                        color: isNegative(transfer) ? '#C51A1A' : 'black',
                    }}>
                    {transfer.toLocaleString(undefined, {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2,
                    })}{' '}
                    $
                </Box>
            );
        },
    };
};