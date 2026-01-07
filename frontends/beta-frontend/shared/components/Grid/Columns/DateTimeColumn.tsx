import ColHeader from "@/shared/components/Grid/Columns/common/components/ColHeader";
import { getColumnConfig } from "@/shared/components/Grid/Columns/common/config/ColumnsConfig";
import { formatDateAndTime } from "@/shared/utils/DateUtils";
import { Typography } from "@mui/material";
import { GridAlignment, GridCellParams, GridColDef } from "@mui/x-data-grid";

export const DateTimeColumn = (
    getDateValue: (row: any) => string,
    alignment: GridAlignment = 'center',
    header
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: header,
        renderHeader: () => <ColHeader header={header} align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const date: string = getDateValue(params.row);
            return (
                <Typography height={'100%'} display={'flex'} sx={{ alignItems: 'center', justifyContent: 'center' }}>
                    {formatDateAndTime(date)}
                </Typography>
            );
        },
    };
};
