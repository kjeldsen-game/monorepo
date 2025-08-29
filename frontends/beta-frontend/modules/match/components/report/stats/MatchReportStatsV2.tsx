import { Box } from '@mui/material'
import { Stats } from 'modules/match/types/MatchResponses';
import React from 'react'
import MatchStatItem from './MatchStatItem';
import CustomButton from '@/shared/components/Common/CustomButton';

interface MatchReportStatsV2 {
    homeStats: Stats;
    awayStats: Stats;
}

const MatchReportStatsV2: React.FC<MatchReportStatsV2> = ({ homeStats, awayStats }) => {
    return (
        <Box width={'100%'} display={'flex'} flexDirection={'column'} alignItems={'center'} pt={2}>
            <MatchStatItem name='Score' awayValue={awayStats?.goals} homeValue={homeStats?.goals} />
            <MatchStatItem name='Total Shots' awayValue={awayStats?.shots} homeValue={homeStats?.shots} />
            <MatchStatItem name='Missed Shots' awayValue={awayStats?.missed} homeValue={homeStats?.missed} />
            <MatchStatItem name='Saves' awayValue={awayStats?.saved} homeValue={homeStats?.saved} />
            <MatchStatItem name='Passes' awayValue={awayStats?.passes} homeValue={homeStats?.passes} />
            <MatchStatItem name='Missed Passes' awayValue={awayStats?.missedPasses} homeValue={homeStats?.missedPasses} />
            <MatchStatItem name='Tackles' awayValue={awayStats?.tackles} homeValue={homeStats?.tackles} />
            <CustomButton sx={{ marginTop: '10px' }} onClick={() => console.log(true)}>
                Show Stats Table
            </CustomButton>
        </Box>
    )
}

export default MatchReportStatsV2