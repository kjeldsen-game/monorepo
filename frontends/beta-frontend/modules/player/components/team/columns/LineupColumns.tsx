import { GridCellParams, GridColDef } from '@mui/x-data-grid';

import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import { Player } from '@/shared/models/player/Player';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';
import CloseIcon from '@mui/icons-material/Close';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import { PlayerNameColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerNameColumn';
import { PlayerAgeColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerAgeColumn';
import { PlayerPositionColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerPositionColumn';
import { PlayerSkillColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerSkillColumn';
import { PlayerSkill } from '@/shared/models/player/PlayerSkill';
import { PlayerOrderColumn } from './PlayerOrderColumn';
import { PlayerOrderSpecColumn } from './PlayerOrderSpecColumn';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import CustomIconButton from '@/shared/components/Common/CustomIconButton';
import { Box } from '@mui/material';

export const LineupColumns = (
    isEditing: boolean,
    handleActionButtonClick: (player: any) => void,
    activePlayer: Player | undefined,
    handlePlayerChange?: (
        value: Player,
        order: PlayerOrder | any,
        field: any,
    ) => void,
    isXs?: boolean,
    isXsPlayers?: boolean,
) => {
    const handlePlayerOrderChange = (
        player: Player,
        value: PlayerOrder,
    ): void => {
        handlePlayerChange?.(player, value, 'playerOrder');
    };

    const handlePlayerOrderSpecChange = (player: Player, value: any): void => {
        handlePlayerChange?.(player, value, 'playerOrderDestinationPitchArea');
    };

    const columns: GridColDef[] = [
        PlayerNameColumn((row) => row, "left"),
        ...((!isXs || !isXsPlayers)
            ? [
                PlayerAgeColumn((row) => row),
                PlayerPositionColumn(
                    (row) => row,
                    'preferredPosition',
                    'Pos'
                )
            ]
            : []
        ),

        PlayerPositionColumn(
            (row) => row,
            'position',
            'Act. Pos',
            'center',
            true
        ),
        ...((!isXs || isXsPlayers) ?
            [PlayerSkillColumn((row) => row, PlayerSkill.SCORING, PlayerSkill.REFLEXES, isXs),
            PlayerSkillColumn((row) => row, PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkill.GOALKEEPER_POSITIONING, isXs),
            PlayerSkillColumn((row) => row, PlayerSkill.BALL_CONTROL, PlayerSkill.INTERCEPTIONS, isXs),
            PlayerSkillColumn((row) => row, PlayerSkill.PASSING, PlayerSkill.CONTROL, isXs),
            PlayerSkillColumn((row) => row, PlayerSkill.AERIAL, PlayerSkill.ORGANIZATION, isXs),
            PlayerSkillColumn((row) => row, PlayerSkill.CONSTITUTION, PlayerSkill.ONE_ON_ONE, isXs),
            PlayerSkillColumn((row) => row, PlayerSkill.TACKLING, undefined, isXs),
            PlayerSkillColumn((row) => row, PlayerSkill.DEFENSIVE_POSITIONING, undefined, isXs)] : []),
        ...((!isXs || !isXsPlayers) ? [
            PlayerOrderColumn((row) => row, 'center', handlePlayerOrderChange, 'PO', isEditing),
            PlayerOrderSpecColumn((row) => row, 'center', handlePlayerOrderSpecChange, 'PO2', isEditing),
            {
                ...getColumnConfig("right"),
                field: 'action',
                renderHeader: () => <ColHeader header={'Action'} align={'right'} />,
                renderCell: (params: GridCellParams) => (
                    <Box sx={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', height: '100%' }}>
                        <CustomIconButton
                            disabled={activePlayer && activePlayer?.id !== params.row.id}
                            sx={{
                                marginRight: '10px',
                            }}
                            onClick={() => {
                                handleActionButtonClick(params.row);
                            }}>
                            {activePlayer?.id === params.row.id ? (
                                <CloseIcon fontSize='small' />
                            ) : (
                                <PeopleAltIcon fontSize='small' />
                            )}
                        </CustomIconButton>
                    </Box>
                ),
            },
        ] : [])


    ];

    return columns;
};
