import styled from '@emotion/styled';

import { useSession } from 'next-auth/react';
import React, { useMemo } from 'react';
import { standingsColumns } from '../Grid/Columns/StandingsColumns';
import Grid from '../Grid/Grid';
import { CircularProgress } from '@mui/material';

interface StandingsTabViewProps {
  league: any;
}

const StandingsTabView: React.FC<StandingsTabViewProps> = ({ league }) => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });
  const memoizedColumnsStadings = useMemo(() => standingsColumns(), []);

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

  if (league == undefined) {
    return <CircularProgress></CircularProgress>;
  }

  return (
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
      }}
      disableColumnMenu={true}
      getRowId={(row) => row.id}
      rows={league}
      columns={memoizedColumnsStadings}
      sortModel={[
        {
          field: 'position',
          sort: 'asc',
        },
      ]}
      getRowClassName={(params) => {
        const { id } = params.row;
        if (id === userData?.user.teamId) {
          return 'super-app-theme--myTeam';
        }
        return '';
      }}
    />
  );
};

export default StandingsTabView;
