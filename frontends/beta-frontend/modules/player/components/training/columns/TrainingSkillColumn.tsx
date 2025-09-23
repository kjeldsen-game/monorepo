import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { ActualSkill, Player } from '@/shared/models/player/Player';
import { Box } from '@mui/material';
import {
    PlayerSkill,
    PlayerSkillToShortcut,
} from '@/shared/models/player/PlayerSkill';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import PlayerSkillText from '@/shared/components/Grid/Columns/common/components/PlayerSkillText';

export const TrainingSkillColumn = (
    getValue: (row: any) => Player,
    primarySkill: PlayerSkill,
    secondarySkill: PlayerSkill | undefined,
    handleCellClick: (
        skillToTrain: PlayerSkill | undefined,
        playerIdToTrain: string,
    ) => void,
    isXs?: boolean,
): GridColDef => {
    return {
        ...getColumnConfig(),
        field: primarySkill,
        ...(isXs ? { width: 30, maxWidth: 30, minWidth: 30 } : {}),
        renderHeader: () => (
            <>
                <ColHeader header={PlayerSkillToShortcut[primarySkill]}>
                    <sup style={{ color: '#FF3F84' }}>
                        {PlayerSkillToShortcut[secondarySkill]}
                    </sup>
                </ColHeader>
            </>
        ),
        renderCell: (params: GridCellParams) => {
            const player: Player = getValue(params.row);
            const skillKey = player.actualSkills[primarySkill]
                ? primarySkill
                : player.actualSkills[secondarySkill!]
                    ? secondarySkill
                    : undefined;

            const skills: ActualSkill | undefined = player.actualSkills[skillKey];

            const skillUnderTraining = params.row.skills;

            return (
                <Box
                    padding={0}
                    flexDirection="column"
                    textAlign="center"
                    sx={{
                        cursor: skillKey ? 'pointer' : "auto",
                        justifyContent: 'center',
                        height: '100%',
                        display: 'flex',
                        alignItems: 'center',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        maxWidth: '100%',
                    }}
                    {...(skillKey && {
                        onClick: () =>
                            handleCellClick(
                                skillKey,
                                params.row.player.id
                            ),
                    })}
                >
                    {skills && <PlayerSkillText
                        data-testid={"player-training-skill-text"}
                        sx={{
                            height: '100%',
                            width: '80px',
                            borderRadius: ' 10px',
                            boxSizing: 'border-box',

                            border: skillUnderTraining.includes(skillKey) ? '3px solid #A4BC10' : '',
                        }}
                        skills={skills} />}
                </Box>
            );
        },
    };
};
