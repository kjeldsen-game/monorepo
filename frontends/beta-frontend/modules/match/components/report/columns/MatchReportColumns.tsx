import { GridColDef } from '@mui/x-data-grid';

import { PlayerNameColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerNameColumn';
import { PlayerSkillColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerSkillColumn';
import { PlayerSkill } from '@/shared/models/player/PlayerSkill';

const MatchReportColumns = (
    isXs: boolean = true
): GridColDef[] => [
        PlayerNameColumn((row) => row, "left"),
        PlayerSkillColumn((row) => row, PlayerSkill.SCORING, PlayerSkill.REFLEXES, isXs),
        PlayerSkillColumn((row) => row, PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkill.GOALKEEPER_POSITIONING, isXs),
        PlayerSkillColumn((row) => row, PlayerSkill.BALL_CONTROL, PlayerSkill.INTERCEPTIONS, isXs),
        PlayerSkillColumn((row) => row, PlayerSkill.PASSING, PlayerSkill.CONTROL, isXs),
        PlayerSkillColumn((row) => row, PlayerSkill.AERIAL, PlayerSkill.ORGANIZATION, isXs),
        PlayerSkillColumn((row) => row, PlayerSkill.CONSTITUTION, PlayerSkill.ONE_ON_ONE, isXs),
        PlayerSkillColumn((row) => row, PlayerSkill.TACKLING, undefined, isXs),
        PlayerSkillColumn((row) => row, PlayerSkill.DEFENSIVE_POSITIONING, undefined, isXs)

    ];

export default MatchReportColumns;
