import { useEffect, useState } from 'react';
import type { NextPage } from 'next';
import { Player } from '@/shared/models/player/Player';
import TeamView from 'modules/player/components/team/TeamView';
import { useTeamApi } from 'modules/player/hooks/api/useTeamApi';
import { useTeamValidateApi } from 'modules/player/hooks/api/useTeamValidateApi';
import { useMatchTeamApi } from 'modules/match/hooks/useMatchTeamApi';
import { mergePlayers } from 'modules/player/utils/LineupUtils';

const Team: NextPage = () => {

  const { data: team } = useTeamApi();
  const { data: teamValidation, mutate: refetchMatchFormation } = useTeamValidateApi();
  const { data: matchTeam, handleUpdateMatchTeamByTeamIdAndMatchId, mutate: refetch } = useMatchTeamApi(false);

  const [teamPlayers, setTeamPlayers] = useState<Player[]>(team?.players ?? []);

  useEffect(() => {
    mergePlayers(team, matchTeam, setTeamPlayers);
  }, [team?.players, matchTeam?.players, matchTeam?.bench]);

  return (
    <TeamView
      handleUpdateLineup={async (request) => { await handleUpdateMatchTeamByTeamIdAndMatchId(request); refetch(); refetchMatchFormation() }}
      team={{
        ...team,
        players: teamPlayers,
        teamModifiers: matchTeam?.modifiers || team?.teamModifiers,
      }}
      teamFormation={teamValidation}
    />
  );
};

export default Team;
