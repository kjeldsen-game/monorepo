import type { NextPage } from 'next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import TeamView from 'modules/player/components/team/TeamView';
import teamFixture from 'modules/player/__fixtures__/team.fixture.json';
import { lineupValidationTrueMock } from 'modules/player/__fixtures__/lineupValidation.fixture';
import { withMockRating } from 'modules/player/__fixtures__/withMockRating';
import { Team as TeamModel } from '@/shared/models/player/Team';

const Team: NextPage = () => {
  const teamValidation = lineupValidationTrueMock;
  const data = {
    ...teamFixture,
    players: withMockRating(teamFixture.players),
  } as unknown as TeamModel;

  return (
    <>
      <TeamView
        teamFormation={teamValidation}
        team={data}
      />
    </>
  );
};

export async function getStaticProps({ locale }: { locale: string }) {
  return {
    props: {
      ...(await serverSideTranslations(locale, ['common', 'game'])),
      // Will be passed to the page component as props
    },
  };
}

export default Team;
