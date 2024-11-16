import type { NextPage } from 'next';
import { Box } from '@mui/material';
import { useSession } from 'next-auth/react';
import EconomyView from '@/shared/components/Economy/EconomyView';
import { useEconomyRepository } from '@/pages/api/economy/useEconomyRepository';

const Economy: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const { data } = useEconomyRepository(
    userData?.user.teamId,
    userData?.accessToken,
  );

  return (
    <>
      <Box>
        <Box
          sx={{
            display: 'flex',
            marginBottom: '2rem',
            alignItems: 'center',
          }}>
          <EconomyView economy={data} />
        </Box>
      </Box>
    </>
  );
};

export default Economy;
