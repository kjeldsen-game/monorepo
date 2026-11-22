import type { NextPage } from 'next';
import { useSession } from '@/shared/mocks/fakeNextAuth';
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
