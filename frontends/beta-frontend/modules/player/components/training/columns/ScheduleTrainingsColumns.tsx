import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { Box } from '@mui/material';
import {
    PlayerSkill,
} from '@/shared/models/player/PlayerSkill';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import { PlayerNameColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerNameColumn';
import { PlayerPositionColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerPositionColumn';
import { TrainingSkillColumn } from './TrainingSkillColumn';
import { renderSkillNames } from 'modules/player/utils/TrainingUtils';


export const ScheduleTrainingsColumns = (
    handleCellClick: (
        skillToTrain: PlayerSkill | undefined,
        skillUnderTraining: PlayerSkill | undefined,
        playerIdToTrain: string,
    ) => void,
    isXs?: boolean,
) => {


    const columns: GridColDef[] = [
        PlayerNameColumn((row) => row.player, "left"),
        PlayerPositionColumn(
            (row) => row.player,
            'preferredPosition',
            'Pos'
        ),
        ...((!isXs) ?
            [
                {
                    field: 'cs',
                    renderHeader: () => <ColHeader header={'CS'} />,
                    ...getColumnConfig(),
                    renderCell: (params: GridCellParams) => (
                        <Box
                            sx={{
                                height: '100%',
                                width: '100%',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                            }}>
                            <Box
                                sx={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    fontWeight: 'bold',
                                    boxSizing: 'border-box',
                                    paddingX: '4px',
                                    width: '80px',
                                    height: '100%',
                                    border: params.row.skills.length != 0 ? '3px solid #A4BC10' : 'none ',
                                    borderRadius: '10px',
                                }}>
                                {renderSkillNames(params.row.skills)}
                            </Box>
                        </Box>
                    ),
                },
            ] : []
        ),
        TrainingSkillColumn((row) => row.player, PlayerSkill.SCORING, PlayerSkill.REFLEXES, handleCellClick, isXs),
        TrainingSkillColumn((row) => row.player, PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkill.GOALKEEPER_POSITIONING, handleCellClick, isXs),
        TrainingSkillColumn((row) => row.player, PlayerSkill.BALL_CONTROL, PlayerSkill.INTERCEPTIONS, handleCellClick, isXs),
        TrainingSkillColumn((row) => row.player, PlayerSkill.PASSING, PlayerSkill.CONTROL, handleCellClick, isXs),
        TrainingSkillColumn((row) => row.player, PlayerSkill.AERIAL, PlayerSkill.ORGANIZATION, handleCellClick, isXs),
        TrainingSkillColumn((row) => row.player, PlayerSkill.CONSTITUTION, PlayerSkill.ONE_ON_ONE, handleCellClick, isXs),
        TrainingSkillColumn((row) => row.player, PlayerSkill.TACKLING, undefined, handleCellClick, isXs),
        TrainingSkillColumn((row) => row.player, PlayerSkill.DEFENSIVE_POSITIONING, undefined, handleCellClick, isXs)
    ];

    return columns;
};
