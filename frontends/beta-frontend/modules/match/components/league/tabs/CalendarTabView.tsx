import { useSession } from 'next-auth/react';
import React, { ChangeEvent, useEffect, useMemo, useState } from 'react';
import { Box } from '@mui/material';
import { Match, MatchStatus } from '@/shared/models/match/Match';
import { CalendarColumns } from '../columns/CalendarColumns';
import { StyledMyTeamDatagrid } from 'modules/match/utils/ChallengeUtils';
import CustomSelectInput from '@/shared/components/Common/CustomSelectInput';
import CalendarFilter from '../filters/CalendarFilter';

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
        () => CalendarColumns(userData?.user.teamId),
        [userData?.user.teamId],
    );

    const handleFilterChange = (event:
        | ChangeEvent<HTMLInputElement>
        | (Event & { target: { value: unknown; name: string } })) => {
        setFilter(event.target.value as string);
    };

    useEffect(() => {
        const newMatches = filter
            ? calendar.filter((row: Match) => row.status === filter)
            : calendar;
        setFilteredMatches(newMatches);
    }, [filter, calendar]);

    return (
        <Box >
            <CalendarFilter
                filter={filter}
                handleFilterChange={handleFilterChange}
            />
            {/* <Box
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
                    <CustomSelectInput
                        title={'Status'}
                        onChange={handleFilterChange}
                        value={filter}
                        values={MatchStatus}
                    />
                </Box>
            </Box> */}

            <StyledMyTeamDatagrid
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
