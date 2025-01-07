import { Box, Tab } from '@mui/material';
import { useState } from 'react';
import DashboardLink from '../DashboardLink';
import { useSession } from 'next-auth/react';
import CustomTabs from '../CustomTabs';
import { CustomTabPanel } from '../Tab/CustomTabPanel';
import CalendarTabView from './CalendarTabView';
import StandingsTabView from './StandingsTabView';

interface LeagueViewProps {
  league: Array<any>;
  calendar: Array<any>;
}

const LeagueView: React.FC<LeagueViewProps> = ({
  league,
  calendar,
}: LeagueViewProps) => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });
  const [selectedTab, setSelectedTab] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue);
  };

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
            <StandingsTabView league={league} />
          </CustomTabPanel>
          <CustomTabPanel value={selectedTab} index={1}>
            <CalendarTabView calendar={calendar} />
          </CustomTabPanel>
        </Box>
      </Box>
    </Box>
  );
};

export default LeagueView;
