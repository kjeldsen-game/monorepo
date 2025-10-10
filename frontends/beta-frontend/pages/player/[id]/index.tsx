import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import PlayerView from 'modules/player/components/player/PlayerView';

const Player: NextPage = () => {
  const { data: userData } = useSession({ required: true });

  return (
    <>
      <PlayerView />
    </>
  );
};

export default Player;
