import Grid from '@/shared/components/Grid/Grid';
import { useSession } from 'next-auth/react';
import { useMatchChallengeActions } from 'modules/match/hooks/useMatchChallengeActions';
import { useMatchChallengeData } from 'modules/match/hooks/useMatchChallengeData';
import { useMemo } from 'react';
import { StyledMyTeamDatagrid } from 'modules/match/utils/ChallengeUtils';
import { useTeamsApi } from 'modules/player/hooks/api/useTeamsApi';
import CreateChallengesColumns from '../columns/CreateChallengesColumns';
import PendingChallengesColumns from '../columns/PendingChallengesColumns';
import NoDataError from '@/shared/components/Grid/no-data-error/NoDataError';
import { EventBusyRounded } from '@mui/icons-material';

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
                slots={{
                    noRowsOverlay: () => (
                        <NoDataError
                            icon={EventBusyRounded}
                            title="No Pending Matches"
                            subtitle="Your team has not any incoming challenges yet. Once they do, they'll appear here."
                        />
                    )
                }}
            />
            <StyledMyTeamDatagrid
                sx={{ paddingTop: 1 }}
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