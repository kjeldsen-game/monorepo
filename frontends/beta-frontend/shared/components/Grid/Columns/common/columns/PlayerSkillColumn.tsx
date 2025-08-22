import { GridCellParams, GridColDef } from '@mui/x-data-grid';

import { ActualSkill, Player } from '@/shared/models/player/Player';
import { Box } from '@mui/material';
import { getColumnConfig } from '../config/ColumnsConfig';
import ColHeader from '../components/ColHeader';
import {
    PlayerSkill,
    PlayerSkillToShortcut,
} from '@/shared/models/player/PlayerSkill';
import PlayerSkillText from '../components/PlayerSkillText';

export const PlayerSkillColumn = (
    getValue: (row: any) => Player,
    primarySkill: PlayerSkill,
    secondarySkill: PlayerSkill | undefined,
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
                    </sup></ColHeader>

            </>
        ),
        renderCell: (params: GridCellParams) => {
            const player: Player = getValue(params.row);
            const skills: ActualSkill | undefined =
                player.actualSkills[primarySkill] ??
                player.actualSkills[secondarySkill!] ??
                undefined;
            return (
                <Box
                    padding={0}
                    flexDirection="column"
                    textAlign="center"
                    sx={{
                        justifyContent: 'center',
                        height: '100%',
                        display: 'flex',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        maxWidth: '100%',
                    }}>
                    {skills && <PlayerSkillText skills={skills} />}
                </Box>
            );
        },
    };
};
