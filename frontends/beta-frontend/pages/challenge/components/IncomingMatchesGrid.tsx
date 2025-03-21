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

interface IncomingMatchesGridProps {
  setAlert: (alert: any) => void;
}

const IncomingMatchesGrid: React.FC<IncomingMatchesGridProps> = ({
  setAlert,
}) => {
  const { data: userData } = useSession({ required: true });

  const { t } = useTranslation('common');

  const router = useRouter();

  const [selectedPage, setSelectedPage] = useState<number>(0);

  const { acceptedMatches, refetch } = useAllPlayerMatchesRepository(
    selectedPage,
    10,
    userData?.user.teamId,
    userData?.accessToken,
  );

  const { executeMatch, declineMatch } = useMatchRepository(
    userData?.accessToken,
    userData?.user.teamId,
  );

  if (!acceptedMatches || acceptedMatches.length === 0) {
    return (
      <Box sx={{ width: '100%' }}>{t('challenge.no_incoming_challenges')}</Box>
    );
  }

  const handleCancelButtonClick = async (matchId: string) => {
    try {
      const response = await declineMatch(matchId);
      if (response.status != 200) {
        setAlert({
          open: true,
          message: response.message,
          type: 'error',
        });
      } else {
        setAlert({
          open: true,
          message: 'Match was successfully canceled!',
          type: 'success',
        });
      }
      refetch();
    } catch (error) {
      console.error('Failed to update team:', error);
    }
  };

  const handlePlayButtonClick = async (matchId: string) => {
    try {
      const response = await executeMatch(matchId);
      if (response.status != 200) {
        setAlert({
          open: true,
          message: response.message,
          type: 'error',
        });
      } else {
        setAlert({
          open: true,
          message: 'Match was successfully executed!',
          type: 'success',
        });
      }
      refetch();
    } catch (error) {
      console.error('Failed to update team:', error);
    }
  };

  return (
    <Box sx={{ width: '100%' }}>
      <Grid
        autoHeight={false}
        isRowSelectable={() => false}
        rows={acceptedMatches ?? []}
        columns={
          userData?.user.teamId
            ? acceptedMatchesColumns(
                handlePlayButtonClick,
                handleCancelButtonClick,
                userData?.user.teamId,
              )
            : []
        }
        sx={{
          maxHeight: '600px',
          minHeight: '400px',
        }}
        // paginationMode="server"
        // pagination
        // pageSize={PAGE_SIZE}
        // hideFooter={false}
        // onPageChange={(value) => setSelectedPage(value)}
        // // TODO: Get total of teams from the API
        // rowCount={10}
      />
    </Box>
  );
};

export { IncomingMatchesGrid };
export default IncomingMatchesGrid;
