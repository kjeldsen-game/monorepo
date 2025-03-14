import Grid from '@/shared/components/Grid/Grid';
import { Box } from '@mui/material';
import { useState } from 'react';
import PendingMatchesColumns from '../../../shared/components/Grid/Columns/Challenge/PendingMatchesColumns';
import { useSession } from 'next-auth/react';
import { useAllPlayerMatchesRepository } from '@/pages/api/match/useAllPlayerMatchesRepository';
import { useTranslation } from 'next-i18next';
import { useMatchRepository } from '@/pages/api/match/useMatchRepository';

const PAGE_SIZE = 10;

interface InviteGridProps {}

const InviteGrid: React.FC<InviteGridProps> = () => {
  const { data: userData } = useSession({ required: true });

  const { t } = useTranslation('common');

  const [selectedPage, setSelectedPage] = useState<number>(0);

  const { pendingMatches } = useAllPlayerMatchesRepository(
    selectedPage,
    10,
    userData?.user.teamId,
    userData?.accessToken,
  );

  const { acceptMatch, declineMatch } = useMatchRepository(
    userData?.accessToken,
    userData?.user.teamId,
  );

  if (!pendingMatches || pendingMatches.length === 0) {
    return (
      <Box sx={{ width: '100%' }}>{t('challenge.no_pending_challenges')}</Box>
    );
  }

  const handleMatchAccept = (matchId: string) => {
    acceptMatch(matchId);
  };

  const handleMatchDecline = (matchId: string) => {
    declineMatch(matchId);
  };

  console.log(pendingMatches);

  return (
    <Box sx={{ width: '100%' }}>
      <Grid
        isRowSelectable={() => false}
        rows={pendingMatches ?? []}
        columns={
          userData?.user.teamId
            ? PendingMatchesColumns(
                userData?.user.teamId,
                handleMatchAccept,
                handleMatchDecline,
              )
            : []
        }
        paginationMode="server"
        pagination
        pageSize={PAGE_SIZE}
        hideFooter={false}
        onPageChange={(value) => setSelectedPage(value)}
        // TODO: Get total of teams from the API
        rowCount={10}
      />
    </Box>
  );
};

export { InviteGrid };
export default InviteGrid;
