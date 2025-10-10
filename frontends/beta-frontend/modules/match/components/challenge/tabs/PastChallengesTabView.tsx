import Grid from '@/shared/components/Grid/Grid'
import { Box } from '@mui/material'
import { useRouter } from 'next/router'
import { useMatchChallengeData } from 'modules/match/hooks/useMatchChallengeData'
import PastChallengesColumns from '../columns/PastChallengesColumns'
import NoDataError from '@/shared/components/Grid/no-data-error/NoDataError'
import { EventBusyRounded } from '@mui/icons-material'


const PastChallengesTabView = () => {

    const router = useRouter();
    const { pastMatches } = useMatchChallengeData();

    const handleReportView = (matchId: string) => {
        router.push(`/match/report/${matchId}`);
    };

    return (
        <Box sx={{ width: '100%' }}>
            <Grid
                initialState={{
                    sorting: { sortModel: [{ field: 'dateTime', sort: 'desc' }] },
                }}
                rows={pastMatches}
                columns={PastChallengesColumns(handleReportView)}
                slots={{
                    noRowsOverlay: () => (
                        <NoDataError
                            icon={EventBusyRounded}
                            title="No Past Challenges"
                            subtitle="Your team has not accepted any challenges yet. Once they do, they'll appear here."
                        />
                    )
                }}
            />
        </Box>
    )
}

export default PastChallengesTabView