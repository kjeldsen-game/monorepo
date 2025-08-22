import Grid from '@/shared/components/Grid/Grid'
import { Box, Typography } from '@mui/material'
import { useRouter } from 'next/router'
import { useMatchChallengeData } from 'modules/match/hooks/useMatchChallengeData'
import PastChallengesColumns from '../columns/PastChallengesColumns'


const PastChallengesTabView = () => {

    const router = useRouter();
    const { pastMatches } = useMatchChallengeData();

    const handleReportView = (matchId: string) => {
        router.push(`/match/report/${matchId}`);
    };

    return (
        <Box sx={{ width: '100%' }}>
            <Box paddingY={2} >
                <Typography fontWeight={'bold'} color={'#555F6C'}>
                    Past Challenges
                </Typography>
            </Box>
            <Grid
                initialState={{
                    sorting: { sortModel: [{ field: 'dateTime', sort: 'desc' }] },
                }}
                rows={pastMatches}
                columns={PastChallengesColumns(handleReportView)}
            />
        </Box>
    )
}

export default PastChallengesTabView