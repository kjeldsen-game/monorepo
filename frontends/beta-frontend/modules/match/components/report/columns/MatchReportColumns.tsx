import { GridColDef } from '@mui/x-data-grid';

import { PlayerNameColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerNameColumn';
import { PlayerSkillColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerSkillColumn';
import { PlayerSkill } from '@/shared/models/player/PlayerSkill';
import { MatchReportSkillColumn } from './MatchReportSkillColumn';

const MatchReportColumns = (
    isXs: boolean = true
): GridColDef[] => [
        PlayerNameColumn((row) => row, "left"),
        MatchReportSkillColumn((row) => row, PlayerSkill.SCORING, PlayerSkill.REFLEXES, isXs),
        MatchReportSkillColumn((row) => row, PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkill.GOALKEEPER_POSITIONING, isXs),
        MatchReportSkillColumn((row) => row, PlayerSkill.BALL_CONTROL, PlayerSkill.INTERCEPTIONS, isXs),
        MatchReportSkillColumn((row) => row, PlayerSkill.PASSING, PlayerSkill.CONTROL, isXs),
        MatchReportSkillColumn((row) => row, PlayerSkill.AERIAL, PlayerSkill.ORGANIZATION, isXs),
        MatchReportSkillColumn((row) => row, PlayerSkill.CONSTITUTION, PlayerSkill.ONE_ON_ONE, isXs),
        MatchReportSkillColumn((row) => row, PlayerSkill.TACKLING, undefined, isXs),
        MatchReportSkillColumn((row) => row, PlayerSkill.DEFENSIVE_POSITIONING, undefined, isXs)
    ];

export default MatchReportColumns;
