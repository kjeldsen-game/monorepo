import { useMatchReportRepository } from '@/pages/api/match/useMatchReportRepository';
import MatchReportContent from '@/shared/components/MatchReport/MatchReportContent';
import MatchReportMetrics from '@/shared/components/MatchReport/MatchReportMetrics';
import { Box, CircularProgress } from '@mui/material';
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

  console.log(report);

  if (!report) {
    return <CircularProgress></CircularProgress>;
  }

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'row',
        width: '100%',
        height: 'calc(100vh - 64px - 42px)',
      }}>
      <MatchReportMetrics
        teamReport={report.home}
        teamColor={'#29B6F6'}
        players={report.home.players}
        teamId={report.home.id}
        stats={report.matchReport.homeStats}
        side="left"
        sx={{
          width: '25%',
        }}
      />
      {report ? (
        <MatchReportContent
          report={report.matchReport}
          sx={{
            width: '50%',
          }}
        />
      ) : null}
      <MatchReportMetrics
        teamReport={report.away}
        teamColor={'#A4BC10'}
        players={report.away.players}
        teamId={report.away.id}
        stats={report.matchReport.awayStats}
        side="right"
        sx={{
          width: '25%',
        }}
      />
    </Box>
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
