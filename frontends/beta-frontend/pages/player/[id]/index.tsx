import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import PlayerView from 'modules/player/components/player/PlayerView';
import PreAlphaAlert from '@/shared/components/PreAlphaAlert';
import { Typography } from '@mui/material';

const Player: NextPage = () => {
  const { data: userData } = useSession({ required: true });

  return (
    <>
      <PreAlphaAlert>
        The player statistics displayed in the table are not yet live.
        Additionally, the functionality of the buttons is still in progress.
      </PreAlphaAlert>
      <PlayerView />
    </>
  );
};

export default Player;
