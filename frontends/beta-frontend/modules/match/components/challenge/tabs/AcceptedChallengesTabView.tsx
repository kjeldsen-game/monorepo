import Grid from '@/shared/components/Grid/Grid'
import { Box } from '@mui/material'
import { useSession } from 'next-auth/react';
import { useMatchChallengeData } from 'modules/match/hooks/useMatchChallengeData';
import { useMatchChallengeActions } from 'modules/match/hooks/useMatchChallengeActions';
import { useMemo } from 'react';
import AcceptedChallengesColumns from '../columns/AcceptedChallengesColumns';

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
            />
        </Box>
    )
}

export default AcceptedChallengesTabView;
