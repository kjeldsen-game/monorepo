import { useSession } from 'next-auth/react';
import React, { useEffect, useMemo, useState } from 'react';
import { calendarColumns } from '../Grid/Columns/CalendarColumns';
import { getBackgroundColor } from '@/shared/utils/EconomyUtils';
import styled from '@emotion/styled';
import Grid from '../Grid/Grid';
import { Box, CircularProgress } from '@mui/material';
import SelectInput from '../Common/SelectInput';
import { Match, MatchStatusEnum } from '@/shared/models/Match';

interface CalendarTabViewProps {
  calendar: any;
}

const CalendarTabView: React.FC<CalendarTabViewProps> = ({ calendar }) => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const [filter, setFilter] = useState<string>();
  const [filteredMatches, setFilteredMatches] = useState<Match[]>([]);

  const memoizedColumnsCalendar = useMemo(
    () => calendarColumns(userData?.user.teamId),
    [userData?.user.teamId],
  );
  console.log(calendar);
  const StyledDataGrid = styled(Grid)(({ theme }) => ({
    '& .super-app-theme--myTeam': {
      backgroundColor: getBackgroundColor('My Team').backgroundColor,
    },
  }));

  const handleFilterChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    setFilter(event.target.value as string);
  };

  useEffect(() => {
    console.log(filter);
    const newMatches = filter
      ? calendar.filter((row: Match) => row.status === filter)
      : calendar;
    console.log(newMatches);
    setFilteredMatches(newMatches);
  }, [filter, calendar]);

  if (!calendar) {
    return <CircularProgress />;
  }

  return (
    <Box>
      <Box
        sx={{
          marginBottom: '2rem',
          alignItems: 'center',
        }}>
        <Box
          sx={{
            padding: '20px',
            borderRadius: '8px',
            background: '#F9F9F9',
          }}>
          <SelectInput
            title={'Status'}
            handleChange={handleFilterChange}
            value={filter}
            values={MatchStatusEnum}
          />
        </Box>
      </Box>

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
            '&:nth-of-type(2), &:nth-of-type(4)': {
              backgroundColor: '#FF3F840D',
              color: 'black',
              fontWeight: 'bold',
            },
          },
          '& .super-app-theme--myTeam': {
            backgroundColor: '#e3f2fd',
          },
          '& .MuiDataGrid-cell': {
            '&:nth-of-type(2), &:nth-of-type(4)': {
              backgroundColor: '#FF3F840D',
              color: 'black',
              fontWeight: 'bold',
            },
          },
        }}
        disableColumnMenu={true}
        getRowId={(row) => row.id}
        rows={filteredMatches}
        columns={memoizedColumnsCalendar}
        sortModel={[
          {
            field: 'position',
            sort: 'asc',
          },
        ]}
        getCellClassName={(params) => {
          const { id } = params.row;
          if (id === userData?.user?.teamId) {
            return 'super-app-theme--myTeam';
          }
          return '';
        }}
      />
    </Box>
  );
};

export default CalendarTabView;
