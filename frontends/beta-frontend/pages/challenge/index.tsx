import type { NextPage } from 'next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { LeagueGrid } from './components/LeagueGrid';
import { Box, SnackbarCloseReason, Tab, Tabs, Typography } from '@mui/material';
import { useState } from 'react';
import { CustomTabPanel } from '@/shared/components/Tab/CustomTabPanel';
import { useSession } from 'next-auth/react';
import { InviteGrid } from './components/InviteGrid';
import { useTranslation } from 'next-i18next';
import IncomingMatchesGrid from './components/IncomingMatchesGrid';
import PastMatchesGrid from './components/PastMatchesGrid';
import CustomTabs from '@/shared/components/CustomTabs';
import SnackbarAlert from '@/shared/components/Common/SnackbarAlert';

const Challenge: NextPage = () => {
  useSession({ required: true });

  const { t } = useTranslation('common', { keyPrefix: 'challenge' });

  const [value, setValue] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  const [alert, setAlert] = useState<any>({
    open: false,
    type: 'success',
    message: '',
  });

  const handleClose = (reason?: SnackbarCloseReason) => {
    if (reason === 'clickaway') {
      return;
    }
    setAlert((prev: any) => ({
      ...prev,
      open: false,
    }));
  };

  return (
    <Box sx={{ width: '100%' }}>
      <SnackbarAlert
        handleClose={handleClose}
        open={alert?.open}
        type={alert?.type}
        message={alert?.message}
      />
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <CustomTabs
          selectedTab={value}
          handleChange={handleChange}
          aria-label="basic tabs example">
          <Tab label="Challenge" />
          <Tab label="Accepted challenges" />
          <Tab label="Past matches" />
        </CustomTabs>
      </Box>
      <CustomTabPanel value={value} index={0}>
        <Typography variant="h5">{t('pending_challenges')}</Typography>
        <InviteGrid />
        <Box sx={{ height: '40px' }} />
        <Typography variant="h5">{t('challenge_team')}</Typography>
        <LeagueGrid setAlert={setAlert} />
      </CustomTabPanel>
      <CustomTabPanel value={value} index={1}>
        <IncomingMatchesGrid setAlert={setAlert} />
      </CustomTabPanel>
      <CustomTabPanel value={value} index={2}>
        <PastMatchesGrid />
      </CustomTabPanel>
    </Box>
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

export default Challenge;
