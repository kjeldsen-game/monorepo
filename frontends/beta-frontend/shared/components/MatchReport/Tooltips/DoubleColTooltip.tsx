import { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box, Grid } from '@mui/material';
import TooltipDataCol from './TooltipDataCol';
import { RStats } from '@/shared/models/MatchReport';
import { formatName } from '@/shared/utils/MatchReportUtils';

interface DoubleColTooltipProps {
  children: ReactNode;
  initiatorStats: RStats;
  challengerStats: RStats;
  attackerName?: string;
  defenderName?: string;
}

const DoubleColTooltip = ({
  initiatorStats,
  challengerStats,
  children,
  attackerName,
  defenderName,
}: DoubleColTooltipProps) => {
  return (
    <CustomTooltip
      tooltipContent={
        <Box sx={{ color: 'black' }}>
          <Grid container>
            <TooltipDataCol
              heading={formatName(attackerName)}
              stats={initiatorStats}
              showAll={true}
            />
            <TooltipDataCol
              heading={formatName(defenderName)}
              stats={challengerStats}
              showAll={true}
            />
          </Grid>
        </Box>
      }>
      {children}
    </CustomTooltip>
  );
};

export default DoubleColTooltip;
