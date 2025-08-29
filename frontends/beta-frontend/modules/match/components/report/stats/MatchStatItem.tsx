import { theme } from '@/libs/material/theme';
import { Box, Typography } from '@mui/material'
import React from 'react'

interface MatchStatItemProps {
    name: string;
    homeValue: string | number;
    awayValue: string | number;
}

const MatchStatItem: React.FC<MatchStatItemProps> = ({ name, homeValue, awayValue }) => {
    return (
        <Box
            display={'flex'}
            sx={{ width: '80%' }}
            justifyContent={'space-between'}>
            <Typography fontSize={22} fontWeight={'bold'}>{homeValue}</Typography>
            <Typography fontWeight={'bold'} color={theme.palette.quaternary.main}>{name}</Typography>
            <Typography fontSize={22} fontWeight={'bold'}>{awayValue}</Typography>
        </Box>
    )
}

export default MatchStatItem