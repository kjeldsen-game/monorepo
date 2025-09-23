import CustomTabs from '@/shared/components/CustomTabs';
import DashboardLink from '@/shared/components/DashboardLink';
import { CustomTabPanel } from '@/shared/components/Tab/CustomTabPanel';
import { Alert, Box, Tab, Typography } from '@mui/material';
import { useSession } from 'next-auth/react';
import { useState } from 'react';
import StandingsTabView from './tabs/StandingsTabView';
import CalendarTabView from './tabs/CalendarTabView';
import { useLeagueApi } from 'modules/match/hooks/useLeagueApi';
import { useTeamApi } from 'modules/player/hooks/api/useTeamApi';
import PreAlphaAlert from '@/shared/components/PreAlphaAlert';
import CustomTabsv2 from '@/shared/components/Tabs/CustomTabsv2';
import { useTabManager } from '@/shared/hooks/useTabManager';

interface LeagueViewProps {
}

const LeagueView: React.FC<LeagueViewProps> = ({ }: LeagueViewProps) => {
    const { } = useSession({
        required: true,
        onUnauthenticated() {
            window.location.href = "/auth/signin";
        }
    });

    const { selectedTab, handleTabChange } = useTabManager();

    const { data: teamData } = useTeamApi()
    const { data: leagueData, isLoading, matches } = useLeagueApi(teamData?.leagueId);

    return (<>
        <PreAlphaAlert />
        <Box sx={{ width: '100%', background: 'white' }} padding={2} borderRadius={2} boxShadow={1}>
            <Box sx={{ width: '100%' }}>
                <CustomTabsv2 handleTabChange={handleTabChange} selectedTab={selectedTab} tabs={["Standings", "Calendar"]} />
                <Box>
                    <CustomTabPanel value={selectedTab} index={0}>
                        <StandingsTabView leagueTeams={leagueData?.teams} isLoading={isLoading} />
                    </CustomTabPanel>
                    <CustomTabPanel value={selectedTab} index={1}>
                        <CalendarTabView calendar={matches} />
                    </CustomTabPanel>
                </Box>
            </Box>
        </Box>
    </>
    );
};

export default LeagueView;
