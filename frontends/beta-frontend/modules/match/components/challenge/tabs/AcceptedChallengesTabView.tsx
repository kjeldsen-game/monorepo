import Grid from '@/shared/components/Grid/Grid'
import { Box } from '@mui/material'
import { useSession } from 'next-auth/react';
import { useMatchChallengeData } from 'modules/match/hooks/useMatchChallengeData';
import { useMatchChallengeActions } from 'modules/match/hooks/useMatchChallengeActions';
import { useMemo } from 'react';
import AcceptedChallengesColumns from '../columns/AcceptedChallengesColumns';
import NoDataError from '@/shared/components/Grid/no-data-error/NoDataError';
import { EventBusyRounded } from '@mui/icons-material';

const AcceptedChallengesTabView = () => {
    const { data: userData } = useSession();
    const { acceptedMatches } = useMatchChallengeData();
    const { handleMatchExecute, handleMatchDecline } = useMatchChallengeActions();

    const columns = useMemo(() =>
        AcceptedChallengesColumns(
            userData?.user?.teamId,
            handleMatchExecute,
            handleMatchDecline
        ),
        [userData?.user?.teamId, handleMatchExecute, handleMatchDecline]
    );

    return (
        <Box sx={{ width: '100%' }}>
            <Grid
                initialState={{
                    sorting: { sortModel: [{ field: 'dateTime', sort: 'desc' }] },
                }}
                rows={acceptedMatches}
                columns={columns}
                slots={{
                    noRowsOverlay: () => (
                        <NoDataError
                            icon={EventBusyRounded}
                            title="No Accepted Challenges"
                            subtitle="It looks like your team hasn't accepted any challenges yet."
                        />
                    )
                }}
            />
        </Box>
    )
}

export default AcceptedChallengesTabView;
