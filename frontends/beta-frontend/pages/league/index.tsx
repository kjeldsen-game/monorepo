import type { NextPage } from 'next';
import { Box } from '@mui/material';
import { useLeagueRepository } from '../api/league/useLeagueRepository';
import { useSession } from 'next-auth/react';
import { useTeamRepository } from '../api/team/useTeamRepository';
import { useEffect, useState } from 'react';
import { useLeagueMatchesRepository } from '../api/match/useLeagueMatchesRepository';
import LeagueView from 'modules/match/components/league/LeagueView';

interface League { }

const League: NextPage<League> = ({ }) => {
  const [standings, setStandings] = useState<any>([]);
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });


  return (
    <LeagueView />
  );
};

export default League;
