import { useEffect, useState } from 'react';
import { CircularProgress } from '@mui/material';
import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import { Player } from '@/shared/models/Player';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useTeamRepository } from '@/pages/api/team/useTeamRepository';
import { TeamModifiers } from '@/shared/models/TeamModifiers';
import { useRouter } from 'next/router';
import { useMatchTeamRepository } from '@/pages/api/match/useMatchTeamRepository';
import {
  filterPlayersByStatus,
  filterPlayersByTeam,
} from '@/shared/utils/LineupUtils';
import { useMatchTeamFormationValidationRepository } from '@/pages/api/match/useMatchTeamFormationValidationRepository';
import TeamViewNew from '@/shared/components/Team/TeamViewNew';

const Team: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const { data } = useTeamRepository(
    userData?.user.teamId,
    userData?.accessToken,
  );

  const { data: formationValidation, refetch } =
    useMatchTeamFormationValidationRepository(
      userData?.user.teamId,
      useRouter().query.id as string,
      userData?.accessToken,
    );
  // console.log(formationValidation);

  const { updateMatchTeam, matchTeam, error, isLoading } =
    useMatchTeamRepository(
      useRouter().query.id,
      userData?.user.teamId,
      userData?.accessToken,
    );

  console.log(matchTeam);

  const [alert, setAlert] = useState<any>({
    open: false,
    type: 'success',
    message: '',
  });

  const [teamPlayers, setTeamPlayers] = useState<Player[]>(data?.players ?? []);

  useEffect(() => {
    let lineupPlayers: any[] = [];
    let benchPlayers: any[] = [];
    let inactivePlayers: any[] = [];

    if (!matchTeam?.specificLineup && data?.players) {
      lineupPlayers = filterPlayersByStatus(data?.players, 'ACTIVE');
      benchPlayers = filterPlayersByStatus(data?.players, 'BENCH');
      inactivePlayers = filterPlayersByStatus(data?.players, 'INACTIVE');
    } else {
      if (matchTeam?.specificLineup && data?.players) {
        lineupPlayers = filterPlayersByTeam(
          data?.players,
          matchTeam?.players,
          'ACTIVE',
        );

        benchPlayers = filterPlayersByTeam(
          data?.players,
          matchTeam?.bench,
          'BENCH',
        );

        inactivePlayers =
          data?.players
            .filter(
              (player) =>
                !matchTeam?.players.some(
                  (lineupPlayer: Player) => lineupPlayer.id === player.id,
                ) &&
                !matchTeam?.bench.some(
                  (benchPlayer: Player) => benchPlayer.id === player.id,
                ),
            )
            .map((player) => {
              return {
                ...player,
                position: null,
                status: 'INACTIVE',
              };
            }) ?? [];
      } else {
        lineupPlayers = [];
        benchPlayers = [];
        inactivePlayers = [];
      }
    }
    const allPlayers = [...lineupPlayers, ...benchPlayers, ...inactivePlayers];
    setTeamPlayers(allPlayers);
  }, [data?.players, matchTeam?.players, matchTeam?.bench]);

  const handleMatchTeamUpdate = async (
    players: Player[],
    teamModifiers: TeamModifiers,
  ) => {
    console.log(players);
    try {
      const response = await updateMatchTeam(players, teamModifiers);
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

  if (sessionStatus === 'loading' || !data) return <CircularProgress />;

  return (
    <TeamViewNew
      teamFormationValidation={formationValidation}
      setAlert={setAlert}
      alert={alert}
      isEditing
      team={{
        ...data,
        players: teamPlayers,
        teamModifiers: matchTeam?.modifiers || data?.teamModifiers,
      }}
      onTeamUpdate={handleMatchTeamUpdate}
    />
  );
};

export async function getStaticPaths() {
  return {
    paths: ['/match/lineup/*'],
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
