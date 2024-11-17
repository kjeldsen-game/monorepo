import { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box } from '@mui/material';
import TooltipDataCol from './TooltipDataCol';
import { RStats } from '@/shared/models/MatchReport';

interface SingleColTooltipProps {
  stats: RStats;
  children: ReactNode;
}

const SingleColTooltip = ({ stats, children }: SingleColTooltipProps) => {
  return (
    <CustomTooltip
      tooltipContent={
        <Box sx={{ color: 'black' }}>
          <Box>
            <TooltipDataCol stats={stats} />
          </Box>
        </Box>
      }>
      {children}
    </CustomTooltip>
  );
};

export default SingleColTooltip;
