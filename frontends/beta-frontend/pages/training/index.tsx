import type { NextPage } from 'next';
import Head from 'next/head';
import Collapsible from '@/shared/components/Collapsible';
import Box from '@mui/material/Box';
import Grid from '@/shared/components/Grid/Grid';
import { players } from '@/data/SamplePlayerTraining';
import { sampleTrainingColumn } from '@/data/sampleTrainingColumn';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useSession } from 'next-auth/react';
import { useTrainingRepository } from '../api/training/useTrainingRepository';
import TrainingView from '@/shared/components/Training/TrainingView';
import { usePlayerRepository } from '../api/player/usePlayerRepository';
import { useTeamRepository } from '../api/team/useTeamRepository';
import { useScheduledTrainingRepository } from '../api/training/useScheduledTrainingRepository';

const Training: NextPage = () => {
  const { data: userData } = useSession({ required: true });

  const { data: trainings, isLoading } = useTrainingRepository(
    userData?.user.teamId,
    userData?.accessToken,
  );

  const { data: scheduledTrainings } = useScheduledTrainingRepository(
    userData?.user.teamId,
    userData?.accessToken,
  );

  return (
    <>
      <Head>
        <title>Kjeldsen</title>
        <meta name="description" content="Generated by create next app" />
      </Head>
      <>
        <TrainingView
          isLoading={isLoading}
          trainings={trainings}
          players={scheduledTrainings}
        />
      </>
    </>
  );
};

export async function getStaticProps({ locale }: { locale: string }) {
  return {
    props: {
      ...(await serverSideTranslations(locale, ['common'])),
      // Will be passed to the page component as props
    },
  };
}

export default Training;
