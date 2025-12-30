import { Grid, styled } from "@mui/material";




export const StyledMyTeamDatagrid = styled(Grid)(({ theme }) => ({
  '& .super-app-theme--myTeam': {
    backgroundColor: getMyTeamBackgroundColor('My Team').backgroundColor,
  },
}));
