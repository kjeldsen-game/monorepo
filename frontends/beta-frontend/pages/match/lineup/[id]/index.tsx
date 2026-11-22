import { useEffect, useState } from 'react';
import type { NextPage } from 'next';
import { Player } from '@/shared/models/player/Player';
import { Team as TeamModel } from '@/shared/models/player/Team';
import TeamView from 'modules/player/components/team/TeamView';
import teamFixture from 'modules/player/__fixtures__/team.fixture.json';
import { lineupValidationTrueMock } from 'modules/player/__fixtures__/lineupValidation.fixture';
import { withMockRating } from 'modules/player/__fixtures__/withMockRating';
import { mergePlayers } from 'modules/player/utils/LineupUtils';

const Team: NextPage = () => {
  const team = {
    ...teamFixture,
    players: withMockRating(teamFixture.players),
  } as unknown as TeamModel;
  const teamValidation = lineupValidationTrueMock;
  const matchTeam = undefined;

  const [teamPlayers, setTeamPlayers] = useState<Player[]>(team?.players ?? []);

  useEffect(() => {
    mergePlayers(team, matchTeam, setTeamPlayers);
  }, [team?.players]);

  return (
    <TeamView
      handleUpdateLineup={async () => undefined}
      team={{
        ...team,
        players: teamPlayers,
        teamModifiers: team?.teamModifiers,
      }}
      teamFormation={teamValidation}
    />
  );
};

export default Team;
