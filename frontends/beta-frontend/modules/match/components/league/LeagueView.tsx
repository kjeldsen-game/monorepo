
import { CustomTabPanel } from '@/shared/components/Tabs/CustomTabPanel';
import { Box } from '@mui/material';
import { useSession } from 'next-auth/react';
import StandingsTabView from './tabs/StandingsTabView';
import CalendarTabView from './tabs/CalendarTabView';
import { useLeagueApi } from 'modules/match/hooks/useLeagueApi';
import { useTeamApi } from 'modules/player/hooks/api/useTeamApi';
import { useTabManager } from '@/shared/hooks/useTabManager';
import CustomTabs from '@/shared/components/Tabs/CustomTabs';

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
        <Box sx={{ width: '100%', background: 'white' }} padding={1} borderRadius={2} boxShadow={1}>
            <Box sx={{ width: '100%' }}>
                <CustomTabs sx={{ paddingBottom: 1 }} handleChange={handleTabChange} selectedTab={selectedTab} tabs={["Standings", "Calendar"]} />
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
