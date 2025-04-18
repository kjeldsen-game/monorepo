import Grid from '@/shared/components/Grid/Grid';
import { Box } from '@mui/material';
import { useEffect, useState } from 'react';
import { useSession } from 'next-auth/react';
import { useAllPlayerMatchesRepository } from '@/pages/api/match/useAllPlayerMatchesRepository';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/navigation';
import pastMatchesColumns from '@/shared/components/Grid/Columns/Challenge/PastMatchesColumns';

const PAGE_SIZE = 10;

interface IncomingMatchesGridProps {}

const PastMatchesGrid: React.FC<IncomingMatchesGridProps> = () => {
  const { data: userData } = useSession({ required: true });

  const { t } = useTranslation('common');

  const router = useRouter();

  const [selectedPage, setSelectedPage] = useState<number>(0);

  const { pastMatches, refetch } = useAllPlayerMatchesRepository(
    selectedPage,
    10,
    userData?.user.teamId,
    userData?.accessToken,
  );

  console.log(pastMatches);

  useEffect(() => {
    refetch();
  }, []);

  if (!pastMatches || pastMatches.length === 0) {
    return <Box sx={{ width: '100%' }}>{t('challenge.no_past_matches')}</Box>;
  }

  const handleReportView = (matchId: string) => {
    router.push(`/match/report/${matchId}`);
  };

  return (
    <Box sx={{ width: '100%' }}>
      <Grid
        autoHeight={false}
        initialState={{
          sorting: { sortModel: [{ field: 'dateTime', sort: 'desc' }] },
        }}
        isRowSelectable={() => false}
        rows={pastMatches ?? []}
        columns={
          userData?.user.teamId ? pastMatchesColumns(handleReportView) : []
        }
        sx={{
          maxHeight: '800px',
          minHeight: '600px',
        }}
        // paginationMode="server"
        // pagination
        // pageSize={PAGE_SIZE}
        // hideFooter={false}
        // onPageChange={(value) => setSelectedPage(value)}
        // TODO: Get total of teams from the API
        // rowCount={10}
      />
    </Box>
  );
};

export { PastMatchesGrid as IncomingMatchesGrid };
export default PastMatchesGrid;
