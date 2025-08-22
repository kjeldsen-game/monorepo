import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import { Player } from '@/shared/models/player/Player';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import CustomSelectInput from '@/shared/components/Common/CustomSelectInput';
import { PitchArea, PitchAreaShortcuts } from 'modules/player/types/Player';

export const PlayerOrderSpecColumn = (
    getPlayerValue: (row: any) => Player,
    alignment: GridAlignment = 'center',
    handlePlayerOrderSpecChange: (player: Player, value) => void,
    header: string,
    isEditing: boolean,
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: header,
        renderHeader: () => <ColHeader header={header} align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const player: Player = getPlayerValue(params.row);
            return (
                <>
                    {!isEditing ? (
                        <div>{PitchAreaShortcuts[player.playerOrderDestinationPitchArea]}</div>
                    ) : (
                        player.playerOrder === "PASS_TO_AREA" || player.playerOrder === "DRIBBLE_TO_AREA" ? (
                            <CustomSelectInput
                                value={player.playerOrderDestinationPitchArea || ''}
                                values={PitchArea}
                                sx={{ width: '80px', height: '30px' }}
                                onChange={(event) =>
                                    handlePlayerOrderSpecChange(params.row, event.target.value)
                                }
                            />
                        ) : null
                    )}
                </>
            );
        },
    };
};