import { useEffect, useState } from 'react';
import { Box, CircularProgress, Tab } from '@mui/material';
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
import { useMatchReportRepository } from '@/pages/api/match/useMatchReportRepository';
import { CustomTabPanel } from '@/shared/components/Tab/CustomTabPanel';
import CustomTabs from '@/shared/components/CustomTabs';

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

  const { data: match, refetch: refetchMatch } = useMatchReportRepository(
    useRouter().query.id,
    userData?.accessToken,
  );

  // console.log(match);

  const { updateMatchTeam } = useMatchTeamRepository(
    useRouter().query.id,
    userData?.user.teamId,
    userData?.accessToken,
  );

  const [alert, setAlert] = useState<any>({
    open: false,
    type: 'success',
    message: '',
  });

  const [teamPlayers, setTeamPlayers] = useState<Player[]>(data?.players ?? []);
  const [teamPlayers2, setTeamPlayers2] = useState<Player[]>(
    data?.players ?? [],
  );
  const [selectedTab, setSelectedTab] = useState(0);

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue);
  };

  useEffect(() => {
    let lineupPlayers: any[] = [];
    let benchPlayers: any[] = [];
    let inactivePlayers: any[] = [];

    if (!match?.away?.specificLineup && data?.players) {
      lineupPlayers = filterPlayersByStatus(data?.players, 'ACTIVE');
      benchPlayers = filterPlayersByStatus(data?.players, 'BENCH');
      inactivePlayers = filterPlayersByStatus(data?.players, 'INACTIVE');
    } else {
      if (match?.away && data?.players) {
        lineupPlayers = filterPlayersByTeam(
          data?.players,
          match?.away?.players,
          'ACTIVE',
        );

        benchPlayers = filterPlayersByTeam(
          data?.players,
          match?.away?.bench,
          'BENCH',
        );

        inactivePlayers =
          data?.players
            .filter(
              (player) =>
                !match?.away?.players.some(
                  (lineupPlayer: Player) => lineupPlayer.id === player.id,
                ) &&
                !match?.away?.bench.some(
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
    setTeamPlayers2(allPlayers);
  }, [data?.players, match?.away?.players, match?.away?.bench]);

  useEffect(() => {
    let lineupPlayers: any[] = [];
    let benchPlayers: any[] = [];
    let inactivePlayers: any[] = [];
    console.log('home usestate');

    if (!match?.home?.specificLineup && data?.players) {
      lineupPlayers = filterPlayersByStatus(data?.players, 'ACTIVE');
      benchPlayers = filterPlayersByStatus(data?.players, 'BENCH');
      inactivePlayers = filterPlayersByStatus(data?.players, 'INACTIVE');
    } else {
      if (match?.home?.specificLineup && data?.players) {
        lineupPlayers = filterPlayersByTeam(
          data?.players,
          match?.home?.players,
          'ACTIVE',
        );

        benchPlayers = filterPlayersByTeam(
          data?.players,
          match?.home?.bench,
          'BENCH',
        );

        inactivePlayers =
          data?.players
            .filter(
              (player) =>
                !match?.home?.players.some(
                  (lineupPlayer: Player) => lineupPlayer.id === player.id,
                ) &&
                !match?.home?.bench.some(
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
  }, [data?.players, match?.home?.players, match?.home?.bench]);

  const handleMatchTeamUpdate = async (
    players: Player[],
    teamModifiers: TeamModifiers,
  ) => {
    // console.log('home chaneajkea');
    // console.log(players);
    try {
      const response = await updateMatchTeam(players, teamModifiers);
      refetchMatch();
      console.log(response);
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

  const handleMatchTeamUpdateAway = async (
    players: Player[],
    teamModifiers: TeamModifiers,
  ) => {
    // console.log('away chaneajkea');
    // console.log(players);
    try {
      const response = await updateMatchTeam(players, teamModifiers, true);
      refetchMatch();
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
    <>
      <Box
        sx={{
          justifyItems: 'center',
        }}>
        <CustomTabs selectedTab={selectedTab} handleChange={handleTabChange}>
          <Tab label="Home" />
          <Tab label="Away" />
        </CustomTabs>
      </Box>
      <CustomTabPanel value={selectedTab} index={0}>
        <TeamViewNew
          teamFormationValidation={formationValidation}
          setAlert={setAlert}
          alert={alert}
          isEditing
          team={{
            ...data,
            players: teamPlayers,
            teamModifiers: match?.home?.modifiers || data?.teamModifiers,
          }}
          onTeamUpdate={handleMatchTeamUpdate}
        />
      </CustomTabPanel>
      <CustomTabPanel value={selectedTab} index={1}>
        <TeamViewNew
          teamFormationValidation={formationValidation}
          setAlert={setAlert}
          alert={alert}
          isEditing
          team={{
            ...data,
            players: teamPlayers2,
            teamModifiers: match?.away?.modifiers || data?.teamModifiers,
          }}
          onTeamUpdate={handleMatchTeamUpdateAway}
        />
      </CustomTabPanel>
    </>
  );
};

export async function getStaticPaths() {
  return {
    paths: ['/match/self/*'],
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
