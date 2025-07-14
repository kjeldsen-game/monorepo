import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import { Player } from '@/shared/models/player/Player';
import ColHeader from '../components/ColHeader';
import { getColumnConfig } from '../config/ColumnsConfig';

export const PlayerAgeColumn = (
    getPlayerValue: (row: any) => Player,
    alignment: GridAlignment = 'center',
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: 'age',
        renderHeader: () => <ColHeader header="Age" align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const player: Player = getPlayerValue(params.row);
            return (
                <div>{player.age.years}</div>
            );
        },
    };
};
