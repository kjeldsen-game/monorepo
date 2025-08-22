import { useTeamRepository } from '@/pages/api/team/useTeamRepository';
import { Player } from '@/shared/models/player/Player';
import { CircularProgress } from '@mui/material';
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

  const { data } = useTeamRepository(
    useRouter().query.id as string,
    userData?.accessToken,
  );

  const [teamPlayers, setTeamPlayers] = useState<Player[]>(data?.players ?? []);

  useEffect(() => {
    setTeamPlayers(data?.players ?? []);
  }, [data?.players]);

  if (sessionStatus === 'loading' || !data) return <CircularProgress />;

  return (
    <>
      <TeamViewNew isEditing={false} team={{ ...data, players: teamPlayers }} />
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
