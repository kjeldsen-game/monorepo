import type { NextPage } from 'next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useSession } from 'next-auth/react';
import PreAlphaAlert from '@/shared/components/PreAlphaAlert';
import TrainingViewV2 from 'modules/player/components/training/TrainingView';

const Training: NextPage = () => {
  const { } = useSession({ required: true });

  return (
    <>
      <PreAlphaAlert />
      <TrainingViewV2 />
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
