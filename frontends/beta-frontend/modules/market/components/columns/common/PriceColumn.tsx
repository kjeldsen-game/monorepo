import ColHeader from "@/shared/components/Grid/Columns/common/components/ColHeader";
import { getColumnConfig } from "@/shared/components/Grid/Columns/common/config/ColumnsConfig";
import { Box, Typography } from "@mui/material";
import { GridAlignment, GridCellParams, GridColDef } from "@mui/x-data-grid";

export const PriceColumn = (
    getValue: (row: any) => string,
    alignment: GridAlignment = 'center',
    header
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: header,
        renderHeader: () => <ColHeader header={header} align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const value: string = getValue(params.row);
            return (
                <Typography height={'100%'} fontWeight={'bold'} display={'flex'} justifyContent={'center'} alignItems={'center'}>
                    {value || 0} $
                </Typography>
            );
        },
    };
};
