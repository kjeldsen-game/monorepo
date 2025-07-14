import type { NextPage } from 'next';
import { Box } from '@mui/material';
import { useSession } from 'next-auth/react';
import { useEconomyRepository } from '@/pages/api/economy/useEconomyRepository';
import EconomyView from '@/shared/components/Economy/views/EconomyView';

const Economy: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const { data } = useEconomyRepository(
    userData?.user.teamId,
    userData?.accessToken,
  );

  console.log(data)

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
