import Grid from '@/shared/components/Grid/Grid';
import { Box } from '@mui/material';
import { useState } from 'react';
import { useSession } from 'next-auth/react';
import { useAllPlayerMatchesRepository } from '@/pages/api/match/useAllPlayerMatchesRepository';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/navigation';
import { useMatchRepository } from '@/pages/api/match/useMatchRepository';
import acceptedMatchesColumns from '@/shared/components/Grid/Columns/Challenge/AcceptedMatchesColumns';

const PAGE_SIZE = 10;

interface IncomingMatchesGridProps {}

const IncomingMatchesGrid: React.FC<IncomingMatchesGridProps> = () => {
  const { data: userData } = useSession({ required: true });

  const { t } = useTranslation('common');

  const router = useRouter();

  const [selectedPage, setSelectedPage] = useState<number>(0);

  const { acceptedMatches } = useAllPlayerMatchesRepository(
    selectedPage,
    10,
    userData?.user.teamId,
    userData?.accessToken,
  );

  const { executeMatch } = useMatchRepository(
    userData?.accessToken,
    userData?.user.teamId,
  );

  if (!acceptedMatches || acceptedMatches.length === 0) {
    return (
      <Box sx={{ width: '100%' }}>{t('challenge.no_incoming_challenges')}</Box>
    );
  }

  const handlePlayButtonClick = (matchId: string) => {
    executeMatch(matchId);
  };

  console.log(acceptedMatches);

  return (
    <Box sx={{ width: '100%' }}>
      <Grid
        isRowSelectable={() => false}
        rows={acceptedMatches ?? []}
        columns={
          userData?.user.teamId
            ? acceptedMatchesColumns(
                handlePlayButtonClick,
                userData?.user.teamId,
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

export { IncomingMatchesGrid };
export default IncomingMatchesGrid;
