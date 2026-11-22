import type { NextPage } from 'next';
import { useSession } from '@/shared/mocks/fakeNextAuth';
import { useTranslation } from 'next-i18next';
import ChallengeView from 'modules/match/components/challenge/ChallengeView';

const Challenge: NextPage = () => {
  useSession({ required: true });

  const { t } = useTranslation('common', { keyPrefix: 'challenge' });

  return (
    <ChallengeView />
  );
};

export default Challenge;
