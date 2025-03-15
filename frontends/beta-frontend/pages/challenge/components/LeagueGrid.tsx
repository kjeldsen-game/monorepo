import { useAllTeamsRepository } from '../../api/team/useAllTeamsRepository';
import Grid from '@/shared/components/Grid/Grid';
import { Box, styled } from '@mui/material';
import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import { Moment } from 'moment';
import { useSession } from 'next-auth/react';
import { useAllPlayerMatchesRepository } from '@/pages/api/match/useAllPlayerMatchesRepository';
import { useMatchRepository } from '@/pages/api/match/useMatchRepository';
import challengeMatchesColumns from '@/shared/components/Grid/Columns/Challenge/ChallengeMatchesColumns';

const PAGE_SIZE = 100;

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

  const getBackgroundColor = (status: string) => {
    switch (status) {
      case 'My Team':
        return {
          backgroundColor: '#FF3F840D',
        };
      default:
        return {
          backgroundColor: 'transparent',
        };
    }
  };

  const StyledDataGrid = styled(Grid)(({ theme }) => ({
    '& .super-app-theme--myTeam': {
      backgroundColor: getBackgroundColor('My Team').backgroundColor,
    },
  }));

  return (
    <Box sx={{ width: '100%' }}>
      <StyledDataGrid
        sx={{
          '& .MuiDataGrid-columnSeparator': {
            display: 'none',
          },
          '& .MuiDataGrid-columnHeaders': {
            padding: 0,
          },
          '& .MuiDataGrid-columnHeader': {
            padding: 0,
          },
          maxHeight: '600px',
          minHeight: '400px',
        }}
        autoHeight={false}
        disableColumnMenu={true}
        getRowId={(row) => row.id}
        rows={allTeams ?? []}
        columns={challengeMatchesColumns(
          t,
          handleChallengeButtonClick,
          allMatches?.map((match) => new Date(match.dateTime).getTime()) ?? [],
        )}
        sortModel={[
          {
            field: 'position',
            sort: 'asc',
          },
        ]}
        getRowClassName={(params) => {
          // console.log(params.row);
          const { id } = params.row;
          console.log(id);
          if (id === userData?.user.teamId) {
            return 'super-app-theme--myTeam';
          }
          return '';
        }}
      />
      {/* <Grid
        isRowSelectable={() => false}
        rows={allTeams ?? []}
        columns={challengeMatchesColumns(
          t,
          handleChallengeButtonClick,
          allMatches?.map((match) => new Date(match.dateTime).getTime()) ?? [],
        )}
        // paginationMode="server"
        // pagination
        // pageSize={10}
        hideFooter={false}
        // onPageChange={(value) => setSelectedPage(value)}
        // TODO: Get total of teams from the API
        // rowCount={10} */}
      {/* /> */}
    </Box>
  );
};

export { LeagueGrid };
export default LeagueGrid;
