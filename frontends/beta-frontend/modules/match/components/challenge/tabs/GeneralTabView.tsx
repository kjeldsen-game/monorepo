import Grid from '@/shared/components/Grid/Grid';
import { useSession } from 'next-auth/react';
import { useMatchChallengeActions } from 'modules/match/hooks/useMatchChallengeActions';
import { useMatchChallengeData } from 'modules/match/hooks/useMatchChallengeData';
import { useMemo, useRef, useState } from 'react';
import { StyledMyTeamDatagrid } from 'modules/match/utils/ChallengeUtils';
import CreateChallengesColumns from '../columns/CreateChallengesColumns';
import PendingChallengesColumns from '../columns/PendingChallengesColumns';
import NoDataError from '@/shared/components/Grid/no-data-error/NoDataError';
import { EventBusyRounded } from '@mui/icons-material';
import { usePageableTeamsApi } from 'modules/player/hooks/api/usePageableTeamsApi';

const GeneralTabView = () => {
    const { data: userData } = useSession();
    const [paginationModel, setPaginationModel] = useState({
        page: 0,
        pageSize: 10,
    });

    const { data, isLoading: loading } = usePageableTeamsApi(paginationModel.page, paginationModel.pageSize);
    const rowCountRef = useRef(data?.totalElements || 0);

    const rowCount = useMemo(() => {
        if (data?.totalElements !== undefined) {
            rowCountRef.current = data.totalElements;
        }
        return rowCountRef.current;
    }, [data?.totalElements]);


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
                sx={{ paddingTop: 1, maxHeight: 300 }}
                disableColumnMenu={true}
                loading={loading}
                getRowId={(row) => row.id}
                rowCount={rowCount}
                paginationMode='server'
                pagination={true}
                paginationModel={paginationModel}
                onPaginationModelChange={setPaginationModel}
                columns={createChallengesColumns}
                rows={data?.content}
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