import type { NextPage } from 'next';
import { Box } from '@mui/material';
import LeagueView from '@/shared/components/League/LeagueView';
import { useLeagueRepository } from '../api/league/useLeagueRepository';
import { useSession } from 'next-auth/react';
import { useTeamRepository } from '../api/team/useTeamRepository';
import { useEffect, useState } from 'react';
import { useLeagueMatchesRepository } from '../api/match/useLeagueMatchesRepository';

interface League { }

const League: NextPage<League> = ({ }) => {
  const [standings, setStandings] = useState<any>([]);
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const {
    data: teamData,
    error,
    isLoading,
  } = useTeamRepository(userData?.user.teamId, userData?.accessToken);

  const { data: leagueData } = useLeagueRepository(
    teamData?.leagueId,
    userData?.accessToken,
  );

  const { data: leagueMatches } = useLeagueMatchesRepository(
    teamData?.leagueId,
    userData?.accessToken,
  );

  useEffect(() => {
    if (leagueData?.teams) {
      const arrayWithIds = Object.entries(leagueData?.teams).map(
        ([id, obj]) => ({
          id,
          ...obj,
        }),
      );
      setStandings(arrayWithIds);
    }
  }, leagueData?.teams);

  return (
    <>
      <Box>
        <Box
          sx={{
            display: 'flex',
            marginBottom: '2rem',
            alignItems: 'center',
          }}>
          <LeagueView league={standings} calendar={leagueMatches} />
        </Box>
      </Box>
    </>
  );
};

export default League;
