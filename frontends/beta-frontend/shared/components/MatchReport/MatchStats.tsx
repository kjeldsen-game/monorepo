import { Box, Typography } from '@mui/material';
import React from 'react';

interface MatchStatsProps {
  report: any;
}

const MatchStats: React.FC<MatchStatsProps> = ({ report }) => {
  return (
    <Box
      display={'flex'}
      flexDirection={'column'}
      alignItems={'center'}
      width={'400px'}
      sx={{ background: '#F8F8F8', padding: '20px' }}>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>{report.matchReport.homeScore}</Typography>
        <Typography>Score</Typography>
        <Typography>{report.matchReport.awayScore}</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Possesion %</Typography>
        <Typography>-</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Shots</Typography>
        <Typography>-</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Shots on target</Typography>
        <Typography>-</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Passes</Typography>
        <Typography>-</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Tackles</Typography>
        <Typography>-</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Yellow cards</Typography>
        <Typography>-</Typography>
      </Box>
    </Box>
  );
};

export default MatchStats;
