import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import LeagueView from 'modules/match/components/league/LeagueView';

interface League { }

const League: NextPage<League> = ({ }) => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  return (
    <LeagueView />
  );
};

export default League;
