import Grid from '@/shared/components/Grid/Grid';
import { Box, Typography } from '@mui/material';
import { useSession } from 'next-auth/react';
import { useMatchChallengeActions } from 'modules/match/hooks/useMatchChallengeActions';
import { useMatchChallengeData } from 'modules/match/hooks/useMatchChallengeData';
import { useMemo } from 'react';
import { StyledMyTeamDatagrid } from 'modules/match/utils/ChallengeUtils';
import { useTeamsApi } from 'modules/player/hooks/api/useTeamsApi';
import CreateChallengesColumns from '../columns/CreateChallengesColumns';
import PendingChallengesColumns from '../columns/PendingChallengesColumns';

const GeneralTabView = () => {
    const { data: userData } = useSession();
    const { data } = useTeamsApi();

    const { handleMatchAccept: acceptMatch, handleMatchDecline: declineMatch, handleMatchCreate: createMatch } = useMatchChallengeActions();
    const { pendingMatches, isLoading } = useMatchChallengeData()
    const createChallengesColumns = useMemo(() => CreateChallengesColumns(createMatch), [createMatch]);

    const handleMatchAccept = (matchId: string) => {
        acceptMatch(matchId);
    };

    const handleMatchDecline = (matchId: string) => {
        declineMatch(matchId);
    };

    return (
        <>
            <Box paddingY={2} >
                <Typography fontWeight={'bold'} color={'grey'}>
                    Pending Challenges
                </Typography>
            </Box>
            <Grid
                loading={isLoading}
                rows={pendingMatches}
                columns={
                    PendingChallengesColumns(
                        userData?.user.teamId,
                        handleMatchAccept,
                        handleMatchDecline
                    )
                }
            />
            <Box paddingY={2} >
                <Typography fontWeight={'bold'} color={'grey'}>
                    Challenge a team
                </Typography>
            </Box>
            <StyledMyTeamDatagrid
                loading={isLoading}
                disableColumnMenu={true}
                getRowId={(row) => row.id}
                rows={data}
                columns={createChallengesColumns}
                sortModel={[
                    {
                        field: 'position',
                        sort: 'asc',
                    },
                ]}
                getRowClassName={(params) => {
                    const { id } = params.row;
                    if (id === userData?.user.teamId) {
                        return 'super-app-theme--myTeam';
                    }
                    return '';
                }}
            />
        </>
    )
}

export default GeneralTabView