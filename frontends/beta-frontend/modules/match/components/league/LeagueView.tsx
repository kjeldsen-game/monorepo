import CustomTabs from '@/shared/components/CustomTabs';
import DashboardLink from '@/shared/components/DashboardLink';
import { CustomTabPanel } from '@/shared/components/Tab/CustomTabPanel';
import { Box, Tab } from '@mui/material';
import { useSession } from 'next-auth/react';
import { useState } from 'react';
import StandingsTabView from './tabs/StandingsTabView';
import CalendarTabView from './tabs/CalendarTabView';
import { useLeagueApi } from 'modules/match/hooks/useLeagueApi';
import { useTeamApi } from 'modules/player/hooks/useTeamApi';

interface LeagueViewProps {
    // league: Array<any>;
    // calendar: Array<any>;
}

const LeagueView: React.FC<LeagueViewProps> = ({
    // league,
    // calendar,
}: LeagueViewProps) => {
    const { data: userData, status: sessionStatus } = useSession({
        required: true,
    });
    const [selectedTab, setSelectedTab] = useState(0);

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setSelectedTab(newValue);
    };

    const { data: teamData } = useTeamApi()
    const { data: leagueData, isLoading, matches } = useLeagueApi(teamData?.leagueId);

    console.log(matches)

    return (
        <Box sx={{ width: '100%' }}>
            <DashboardLink children={'Back to Dashboard'} />
            <Box sx={{ width: '100%' }}>
                <Box>
                    <CustomTabs selectedTab={selectedTab} handleChange={handleChange}>
                        <Tab label="Standings" />
                        <Tab label="Calendar" />
                    </CustomTabs>
                </Box>

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
    );
};

export default LeagueView;
