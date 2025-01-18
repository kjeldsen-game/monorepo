import { useEffect, useState } from 'react';
import TeamView from '@/shared/components/TeamView';
import { CircularProgress } from '@mui/material';
import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import { useTeamRepository } from '../api/team/useTeamRepository';
import { Player } from '@/shared/models/Player';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { TeamModifiers } from '@/shared/models/TeamModifiers';
import { useTeamFormationValidationRepository } from '../api/team/useTeamFormationValidationRepository';

const Team: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const { data, updateTeam } = useTeamRepository(
    userData?.user.teamId,
    userData?.accessToken,
  );

  const { data: formationValidation, refetch } =
    useTeamFormationValidationRepository(
      userData?.user.teamId,
      userData?.accessToken,
    );

  const [alert, setAlert] = useState<any>({
    open: false,
    type: 'success',
    message: '',
  });

  const [teamPlayers, setTeamPlayers] = useState<Player[]>(data?.players ?? []);

  useEffect(() => {
    setTeamPlayers(data?.players ?? []);
  }, [data?.players]);

  if (sessionStatus === 'loading' || !data) return <CircularProgress />;

  const handleTeamUpdate = async (
    players: Player[],
    teamModifiers: TeamModifiers,
  ) => {
    try {
      const response = await updateTeam(players, teamModifiers);
      if (response.status == 500 || response.status == 400) {
        setAlert({
          open: true,
          message: response.message,
          type: 'error',
        });
      } else {
        setAlert({
          open: true,
          message: 'Team was updated successfully!',
          type: 'success',
        });
        refetch();
      }
    } catch (error) {
      console.error('Failed to update team:', error);
    }
  };

  return (
    <TeamView
      setAlert={setAlert}
      alert={alert}
      teamFormationValidation={formationValidation}
      isEditing
      team={{ ...data, players: teamPlayers }}
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
