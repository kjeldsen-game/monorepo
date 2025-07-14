import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { Player } from '@/shared/models/player/Player';
import { getColumnConfig } from '../config/ColumnsConfig';
import ColHeader from '../components/ColHeader';
import PlayerPositionLabel from '@/shared/components/Player/PlayerPositionLabel';

export type PlayerPositionType = {
    [K in keyof Player]: Player[K] extends PlayerPosition | undefined ? K : never;
}[keyof Player];

export const PlayerPositionColumn = (
    getPlayerValue: (row: any) => Player,
    positionType: PlayerPositionType = 'preferredPosition',
    header: string = 'Pos',
    alignment: GridAlignment = 'center'
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: `${positionType}`,
        // minWidth: 100,
        renderHeader: () => <ColHeader header={header} align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const player: Player = getPlayerValue(params.row);
            const position = player[positionType] as PlayerPosition;

            return <PlayerPositionLabel position={position} />;
        },
    };
};
