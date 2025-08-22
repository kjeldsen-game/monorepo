import Grid from "@/shared/components/Grid/Grid";
import { styled } from "@mui/material";
import { getBackgroundColor } from "modules/player/utils/EconomyUtils";

export const StyledEconomyDatagrid = styled(Grid)(({ theme }) => ({
    '& .super-app-theme--TotalIncome': {
        backgroundColor: getBackgroundColor('Total Income').backgroundColor,
        fontWeight: 'bold',
    },
    '& .super-app-theme--TotalOutcome': {
        backgroundColor:
            getBackgroundColor('Total Outcome').backgroundColor,
        fontWeight: 'bold',
    },
    '& .super-app-theme--TotalBalance': {
        backgroundColor:
            getBackgroundColor('Total Balance').backgroundColor,
        fontWeight: 'bold',
    },
}));
