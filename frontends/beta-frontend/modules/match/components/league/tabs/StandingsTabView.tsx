import styled from '@emotion/styled';

import { useSession } from 'next-auth/react';
import React, { useMemo } from 'react';
import { StandingsColumns } from '../columns/StandingsColumns';
import { StyledMyTeamDatagrid } from 'modules/match/utils/ChallengeUtils';
import { LeagueStats } from 'modules/match/types/LeagueResponses';
import { Box, Typography } from '@mui/material';

interface StandingsTabViewProps {
    leagueTeams: Record<string, LeagueStats>;
    isLoading: boolean
}

const StandingsTabView: React.FC<StandingsTabViewProps> = ({ leagueTeams, isLoading }) => {

    const { data: userData } = useSession()
    const memoizedColumnsStadings = useMemo(() => StandingsColumns(), []);

    return (<>
        <StyledMyTeamDatagrid
            loading={isLoading || !leagueTeams}
            disableColumnMenu={true}
            hideFooter
            getRowId={(row) => row.name}
            rows={Object.entries(leagueTeams ?? {}).map(([teamId, stats]) => ({
                ...stats,
                id: teamId,
            }))}
            columns={memoizedColumnsStadings}
            sortModel={[
                {
                    field: 'position',
                    sort: 'asc',
                },
            ]}
            getRowClassName={(params) => {
                if (params.row.id === userData?.user.teamId) {
                    return 'super-app-theme--myTeam';
                }
                return '';
            }}
        />
    </>
    );
};

export default StandingsTabView;
