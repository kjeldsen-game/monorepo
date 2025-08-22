import type { NextPage } from 'next';
import { Box } from '@mui/material';
import { useSession } from 'next-auth/react';
import { useEconomyRepository } from '@/pages/api/economy/useEconomyRepository';
import EconomyView from 'modules/player/components/economy/EconomyView';
import { useEconomyApi } from 'modules/player/hooks/useEconomyApi';

const Economy: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });


  return (
    <EconomyView />
  );
};

export default Economy;
