import { useEffect, useState } from 'react';
import { Box, Tab } from '@mui/material';
import type { NextPage } from 'next';
import { Player } from '@/shared/models/player/Player';
import {
  filterPlayersByStatus,
  filterPlayersByTeam,
} from '@/shared/utils/LineupUtils';
import { CustomTabPanel } from '@/shared/components/Tabs/CustomTabPanel';
import CustomTabs from '@/shared/components/Tabs/CustomTabs';
import { useTeamApi } from 'modules/player/hooks/api/useTeamApi';
import TeamView from 'modules/player/components/team/TeamView';
import { useMatch } from 'modules/match/hooks/useMatch';
import { useMatchTeamApi } from 'modules/match/hooks/useMatchTeamApi';
import { useMatchFormation } from 'modules/match/hooks/useMatchFormation';
import { mergePlayers } from 'modules/player/utils/LineupUtils';


const Team: NextPage = () => {
  const { data: match, refetch } = useMatch();
  const { data } = useTeamApi();

  const { data: teamValidation, mutate: refetchMatchFormation } = useMatchFormation(true);

  const { handleUpdateMatchTeamByTeamIdAndMatchId: handleSelfHomeTeamUpdate } = useMatchTeamApi(false);
  const { handleUpdateMatchTeamByTeamIdAndMatchId: handleSelfAwayTeamUpdate } = useMatchTeamApi(true);

  const [teamPlayers, setTeamPlayers] = useState<Player[]>(data?.players ?? []);
  const [teamPlayers2, setTeamPlayers2] = useState<Player[]>(
    data?.players ?? [],
  );

  const [selectedTab, setSelectedTab] = useState(0);
  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue);
  };

  useEffect(() => {
    mergePlayers(data, match?.home, setTeamPlayers)
  }, [data?.players, match?.home?.players, match?.home?.bench]);

  useEffect(() => {
    mergePlayers(data, match?.away, setTeamPlayers2)
  }, [data?.players, match?.away?.players, match?.away?.bench]);

  return (
    <>
      <Box
        sx={{
          justifyItems: 'center',
        }}>
        <CustomTabs selectedTab={selectedTab} handleChange={handleTabChange}>
          <Tab label="Home" />
          <Tab label="Away" />
        </CustomTabs>
      </Box>
      <CustomTabPanel value={selectedTab} index={0}>
        <TeamView
          handleUpdateLineup={async (request) => { await handleSelfHomeTeamUpdate(request); refetch(); refetchMatchFormation() }}
          teamFormation={teamValidation?.[0]}
          team={{
            ...data,
            players: teamPlayers,
            teamModifiers: match?.home?.modifiers || data?.teamModifiers,
          }}
        />
      </CustomTabPanel>
      <CustomTabPanel value={selectedTab} index={1}>
        <TeamView
          handleUpdateLineup={async (request) => { await handleSelfAwayTeamUpdate(request); refetch(); refetchMatchFormation() }}
          teamFormation={teamValidation?.[1]}
          team={{
            ...data,
            players: teamPlayers2,
            teamModifiers: match?.away?.modifiers || data?.teamModifiers,
          }}
        />
      </CustomTabPanel>
    </>
  );
};

export default Team;
