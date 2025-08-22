import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import TeamView from 'modules/player/components/team/TeamView';
import { useTeamApi } from 'modules/player/hooks/api/useTeamApi';
import { useTeamValidateApi } from 'modules/player/hooks/api/useTeamValidateApi';

const Team: NextPage = () => {
  const { } = useSession({
    required: true,
  });

  const { data: teamValidation } = useTeamValidateApi();
  const { data } = useTeamApi();

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
