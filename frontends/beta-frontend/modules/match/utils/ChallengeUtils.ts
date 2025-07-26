import Grid from '@/shared/components/Grid/Grid';
import { styled } from '@mui/material';

export const getMyTeamBackgroundColor = (status: string) => {
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

export const StyledMyTeamDatagrid = styled(Grid)(({ theme }) => ({
  '& .super-app-theme--myTeam': {
    backgroundColor: getMyTeamBackgroundColor('My Team').backgroundColor,
  },
}));
