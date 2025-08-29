import { Player } from '@/shared/models/player/Player'
import { useMemo } from 'react';
import MatchReportColumns from '../columns/MatchReportColumns';
import Grid from '@/shared/components/Grid/Grid';
import { Box, Icon, Typography, useMediaQuery } from '@mui/material';
import { TeamModifiers } from 'modules/player/types/TeamModifiers';
import ShieldIcon from '@mui/icons-material/Shield';
import ManIcon from '@mui/icons-material/Man';
import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward';
import HealthAndSafetyIcon from '@mui/icons-material/HealthAndSafety';
import { theme } from '@/libs/material/theme';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { TeamRole } from '@/shared/models/MatchReport';

interface MatchReportTeamStatsProps {
    teamRole: TeamRole;
    teamModifiers: TeamModifiers;
    players: Player[]
}

const MatchReportTeamStats: React.FC<MatchReportTeamStatsProps> = ({ players, teamModifiers, teamRole }) => {

    console.log('Players in MatchReportTeamStats:', teamModifiers);
    const memoizedColumns = useMemo(() => MatchReportColumns(), [players]);
    const isXs = useMediaQuery(theme.breakpoints.down('sm'));

    return (
        <Box width={isXs ? '100%' : '25%'} padding={1} sx={{ background: 'white' }} borderRadius={!isXs && 1} boxShadow={!isXs && 1}>
            <Typography fontWeight={'bold'} color={'grey'}>
                {convertSnakeCaseToTitleCase(teamRole)} Team
            </Typography>
            <Box padding={0}>
                <Typography fontWeight={'bold'} color={'grey'}>
                    Team Modifiers
                </Typography>
                <Box display={'flex'} flexDirection={'column'}>
                    <Box display={'flex'} alignContent={'center'}>
                        <HealthAndSafetyIcon />
                        {convertSnakeCaseToTitleCase(teamModifiers?.tactic)}
                    </Box>
                    <Box display={'flex'} alignContent={'center'}>
                        <ManIcon />
                        {convertSnakeCaseToTitleCase(teamModifiers?.verticalPressure)}
                    </Box>
                    <Box display={'flex'} alignContent={'center'}>
                        <ShieldIcon />
                        {convertSnakeCaseToTitleCase(teamModifiers?.horizontalPressure)}
                    </Box>
                </Box>
            </Box>
            <Grid
                hideFooter
                disableColumnMenu={true}
                rows={players?.slice(0, 11) || []}
                columns={memoizedColumns}
            />
        </Box>
    )
}

export default MatchReportTeamStats