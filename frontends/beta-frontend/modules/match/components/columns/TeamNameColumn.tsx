import ColHeader from "@/shared/components/Grid/Columns/common/components/ColHeader";
import ColLink from "@/shared/components/Grid/Columns/common/components/ColLink";
import { getColumnConfig } from "@/shared/components/Grid/Columns/common/config/ColumnsConfig";
import { GridAlignment, GridCellParams, GridColDef } from "@mui/x-data-grid";

export const TeamNameColumn = (
    getTeamNameValue: (row: any) => string,
    alignment: GridAlignment = 'center',
    header: string
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: header,
        renderHeader: () => <ColHeader header={header} align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const paddingStyle =
                alignment === 'left'
                    ? { paddingLeft: '10px' }
                    : alignment === 'right'
                        ? { paddingRight: '10px' }
                        : {};
            const team: any = getTeamNameValue(params.row);
            return (
                <ColLink
                    sx={{ ...paddingStyle }}
                    children={team.name}
                    urlValue={`/team/${team.id}`}
                />
            );
        },
    };
};
