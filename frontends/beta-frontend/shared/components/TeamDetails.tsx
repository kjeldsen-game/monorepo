import Box from '@mui/material/Box';
import { Typography } from '@mui/material';
import Avatar from '@/shared/components/Avatar';
import { SampleTeamStats } from 'data/SampleTeam';
import BarChartIcon from '@mui/icons-material/BarChart';

interface TeamDetailsProps {
  name?: string;
}

import React from 'react';

const TeamDetails = ({ name }: TeamDetailsProps) => {
  return (
    <Box
      sx={{
        display: 'flex',
        alignItems: 'center',
      }}>
      <Avatar />
      <Box
        sx={{
          marginLeft: '3rem',
        }}>
        <Typography variant="body1">
          <strong>{name}</strong>
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <BarChartIcon
            sx={{ fontSize: 'large', color: '#A4BC10', marginRight: '4px' }}
          />
          <Typography variant="body1" sx={{ color: '#A4BC10' }}>
            Team League Position
            {/* {SampleTeam.position} Position */}
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default TeamDetails;
