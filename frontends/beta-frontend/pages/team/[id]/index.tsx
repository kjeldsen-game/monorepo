import { useTeamRepository } from '@/pages/api/team/useTeamRepository';
import TeamView from '@/shared/components/TeamView';
import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';

// eslint-disable-next-line react/prop-types
const Team: NextPage = () => {
  const { data: userData } = useSession({ required: true });
  const router = useRouter();

  const { data } = useTeamRepository(
    router.query.id as string,
    userData?.accessToken,
  );

  console.log(data?.players);

  return (
    <>
      <TeamView isEditing={false} team={data}></TeamView>
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
