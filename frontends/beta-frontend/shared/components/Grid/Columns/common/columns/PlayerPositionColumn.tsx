import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { Player } from '@/shared/models/player/Player';
import { getColumnConfig } from '../config/ColumnsConfig';
import ColHeader from '../components/ColHeader';
import PlayerPositionLabel from '@/shared/components/Player/PlayerPositionLabel';
import { PlayerLineupStatus } from '@/shared/models/player/PlayerLineupStatus';

export type PlayerPositionType = {
    [K in keyof Player]: Player[K] extends PlayerPosition | undefined ? K : never;
}[keyof Player];

export const PlayerPositionColumn = (
    getPlayerValue: (row: any) => Player,
    positionType: PlayerPositionType = 'preferredPosition',
    header: string = 'Pos',
    alignment: GridAlignment = 'center',
    showBenchStatus: boolean = false,
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: `${positionType}`,
        renderHeader: () => <ColHeader header={header} align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const player: Player = getPlayerValue(params.row);
            const status: PlayerLineupStatus | undefined = player.status;
            const position = player[positionType] as PlayerPosition;

            return <PlayerPositionLabel position={status === PlayerLineupStatus.BENCH && showBenchStatus ? status : position} />;
        },
    };
};
