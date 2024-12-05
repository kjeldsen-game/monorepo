import { useEffect, useState } from 'react';
import TeamView from '@/shared/components/TeamView';
import { CircularProgress } from '@mui/material';
import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import { useTeamRepository } from '../api/team/useTeamRepository';
import { Player } from '@/shared/models/Player';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';

// eslint-disable-next-line react/prop-types
const Team: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const { data, updateTeam, error, isLoading } = useTeamRepository(
    userData?.user.teamId,
    userData?.accessToken,
  );

  console.log(error);

  const [teamPlayers, setTeamPlayers] = useState<Player[]>(data?.players ?? []);

  useEffect(() => {
    setTeamPlayers(data?.players ?? []);
  }, [data?.players]);

  if (sessionStatus === 'loading' || !data) return <CircularProgress />;

  const handlePlayerChange = (value: Player) => {
    if (data === undefined) return;
    setTeamPlayers((prev) => {
      const index = prev.findIndex((p) => p.id === value.id);
      const newPlayers = [...prev];
      newPlayers[index] = { ...value };
      return newPlayers;
    });
  };

  const handleTeamUpdate = async () => {
    // console.log(teamPlayers);
    try {
      const response = await updateTeam(teamPlayers);
      if (response.status == 500) {
        // setShowAlert({
        //   open: true,
        //   message: response.message,
        //   type: 'error',
        // });
      } else {
        // setShowAlert({
        //   open: true,
        //   message: 'Pricing updated successfully',
        //   type: 'success',
        // });
      }
    } catch (error) {
      console.error('Failed to update auction:', error);
    }
  };

  return (
    <TeamView
      isEditing
      team={{ ...data, players: teamPlayers }}
      handlePlayerChange={handlePlayerChange}
      onTeamUpdate={handleTeamUpdate}></TeamView>
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
