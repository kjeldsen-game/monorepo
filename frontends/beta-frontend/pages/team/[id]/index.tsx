import TeamView from 'modules/player/components/team/TeamView';
import { useTeamApi } from 'modules/player/hooks/api/useTeamApi';
import { useTeamValidateApi } from 'modules/player/hooks/api/useTeamValidateApi';
import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';

// eslint-disable-next-line react/prop-types
const Team: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const { data: teamValidation } = useTeamValidateApi();
  const { data } = useTeamApi(useRouter().query.id as string);

  return (
    <>
      <TeamView
        teamFormation={teamValidation}
        team={data}
      />
    </>
  );
};

export async function getStaticPaths() {
  return {
    paths: ['/team/*'],
    fallback: true,
  };
}

export async function getStaticProps({ locale }: { locale: string }) {
  return {
    props: {
      ...(await serverSideTranslations(locale, ['common', 'game'])),
      // Will be passed to the page component as props
    },
  };
}

export default Team;
