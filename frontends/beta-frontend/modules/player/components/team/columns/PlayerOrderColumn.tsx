import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import { Player } from '@/shared/models/player/Player';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import CustomSelectInput from '@/shared/components/Common/CustomSelectInput';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';
import { Box } from '@mui/material';

export const PlayerOrderColumn = (
    getPlayerValue: (row: any) => Player,
    alignment: GridAlignment = 'center',
    handlePlayerOrderChange: (player: Player, value) => void,
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
                <Box display={'flex'} justifyContent={'center'} alignItems={'center'} height={'100%'}>
                    {!isEditing ? (
                        <div>{convertSnakeCaseToTitleCase(player.playerOrder)}</div>
                    ) : (
                        <CustomSelectInput
                            value={player.playerOrder || ''}
                            values={PlayerOrder}
                            sx={{ width: '80px', height: '30px' }}
                            onChange={(event) =>
                                handlePlayerOrderChange(params.row, event.target.value)
                            }
                        />
                    )}
                </Box>
            );
        },
    };
};
