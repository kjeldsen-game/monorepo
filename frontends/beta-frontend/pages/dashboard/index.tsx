import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import DashboardView from '@/shared/components/Dashboard/DashboardView';

const Home: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });
  return (
    <DashboardView />
  );
};

export default Home;
