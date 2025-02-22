import Box from '@mui/material/Box';
import { Typography } from '@mui/material';
import Avatar from '@/shared/components/Avatar';
import BarChartIcon from '@mui/icons-material/BarChart';

interface TeamDetailsProps {
  name?: string;
}

const TeamDetails = ({ name }: TeamDetailsProps) => {
  return (
    <Box
      sx={{
        width: '25%',
        display: 'flex-column',
        justifyItems: 'center',
        alignItems: 'center',
      }}>
      <Avatar />

      <Typography variant="body1">
        <strong>{name}</strong>
      </Typography>
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}>
        <BarChartIcon
          sx={{ fontSize: 'large', color: '#A4BC10', marginRight: '4px' }}
        />
        <Typography variant="body1" sx={{ color: '#A4BC10' }}>
          Team League Position
          {/* {SampleTeam.position} Position */}
        </Typography>
      </Box>
    </Box>
  );
};

export default TeamDetails;
