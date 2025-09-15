import { useMatchReportRepository } from '@/pages/api/match/useMatchReportRepository';
import PreAlphaAlert from '@/shared/components/PreAlphaAlert';
import { Box, CircularProgress } from '@mui/material';
import MatchReportView from 'modules/match/components/report/MatchReportView';
import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';

// eslint-disable-next-line react/prop-types
const MatchReport: NextPage = () => {
  const { data } = useSession({ required: true });

  const {
    data: report,
    error,
    isLoading,
  } = useMatchReportRepository(useRouter().query.id, data?.accessToken);


  if (!report) {
    return <CircularProgress></CircularProgress>;
  }

  return (
    <>
      <PreAlphaAlert />
      <MatchReportView />
    </>
  );
};

export async function getStaticPaths() {
  return {
    paths: ['/match/report/*'],
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

export default MatchReport;
