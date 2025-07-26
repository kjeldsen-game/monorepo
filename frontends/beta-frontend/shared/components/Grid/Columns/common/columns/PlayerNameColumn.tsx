import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import { Player } from '@/shared/models/player/Player';
import ColHeader from '../components/ColHeader';
import ColLink from '../components/ColLink';
import { getColumnConfig } from '../config/ColumnsConfig';

export const PlayerNameColumn = (
    getPlayerValue: (row: any) => Player,
    alignment: GridAlignment = 'left',
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: 'name',
        renderHeader: () => <ColHeader header="Name" align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const paddingStyle =
                alignment === 'left'
                    ? { paddingLeft: '10px' }
                    : alignment === 'right'
                        ? { paddingRight: '10px' }
                        : {};
            const player: Player = getPlayerValue(params.row);
            return (
                <ColLink sx={{ ...paddingStyle }} urlValue={`/player/${player.id}`}>
                    {player.name}
                </ColLink>
            );
        },
    };
};
