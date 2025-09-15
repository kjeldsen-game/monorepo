import { GridColDef } from '@mui/x-data-grid';
import { Box } from '@mui/material';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import { PlayerNameColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerNameColumn';
import { PlayerPositionColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerPositionColumn';
import { PlayerSkillToShortcut } from '@/shared/models/player/PlayerSkill';
import ProgressBar from '../progressBar/ProgressBar';
import { SkillDeltaColumn } from './SkillDeltaColumn';


export const TrainingColumns = (isXs: boolean) => {
    const columns: GridColDef[] = [
        PlayerNameColumn((row) => row.player, "left"),
        ...(!isXs ? [
            PlayerPositionColumn(
                (row) => row.player,
                'preferredPosition',
                'Pos'
            )
        ] : []),
        {
            ...getColumnConfig(),
            field: 'cv',
            maxWidth: isXs ? 30 : undefined,
            renderHeader: () => <ColHeader header={'Skill'} />,
            renderCell: (params) => {
                return <Box>
                    {isXs ? PlayerSkillToShortcut[params.row.skill] : convertSnakeCaseToTitleCase(params.row.skill)}
                </Box>;
            },
        },
        SkillDeltaColumn(),
        {
            ...getColumnConfig(),
            field: 'modifier',
            renderHeader: () => <ColHeader header={'Modifier'} secondaryHeader={'MD'} />,
            renderCell: (params) => {
                return (
                    <div>
                        {convertSnakeCaseToTitleCase(params.row.modifier)}
                    </div>
                );
            },
        },
        {
            ...getColumnConfig('right'),
            field: 'day',
            flex: 1,
            renderHeader: () => <ColHeader header={'PB'} align='right' />,
            renderCell: (params) => {
                return (
                    <div style={{ marginRight: '10px', display: ' flex', justifyContent: 'end' }}>
                        <ProgressBar days={params.row.currentDay} />
                    </div>
                );
            },
        },
    ];

    return columns;
};
