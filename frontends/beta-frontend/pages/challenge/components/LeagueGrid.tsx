import { useAllTeamsRepository } from '../../api/team/useAllTeamsRepository';
import Grid from '@/shared/components/Grid/Grid';
import { Box } from '@mui/material';
import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import { Moment } from 'moment';
import { useSession } from 'next-auth/react';
import { useAllPlayerMatchesRepository } from '@/pages/api/match/useAllPlayerMatchesRepository';
import { useMatchRepository } from '@/pages/api/match/useMatchRepository';
import challengeMatchesColumns from '@/shared/components/Grid/Columns/Challenge/ChallengeMatchesColumns';

const PAGE_SIZE = 10;

interface LeagueGridProps {}

const LeagueGrid: React.FC<LeagueGridProps> = () => {
  const [selectedPage, setSelectedPage] = useState<number>(0);

  const { data: userData } = useSession({ required: true });
  const { t } = useTranslation('common');

  const { createMatch } = useMatchRepository(
    userData?.accessToken,
    userData?.user.teamId,
  );
  const { allMatches, refetch } = useAllPlayerMatchesRepository(
    undefined,
    undefined,
    userData?.user.teamId,
    userData?.accessToken,
  );
  const { allTeams } = useAllTeamsRepository(
    selectedPage,
    PAGE_SIZE,
    userData?.accessToken,
  );

  const handleChallengeButtonClick = (id: string, date: Moment) => {
    const ownTeamId = userData?.user.teamId;
    if (!ownTeamId) {
      return;
    }
    createMatch(id, date.toDate());
  };

  return (
    <Box sx={{ width: '100%' }}>
      <Grid
        isRowSelectable={() => false}
        rows={allTeams ?? []}
        columns={challengeMatchesColumns(
          t,
          handleChallengeButtonClick,
          allMatches?.map((match) => new Date(match.dateTime).getTime()) ?? [],
        )}
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

export { LeagueGrid };
export default LeagueGrid;
