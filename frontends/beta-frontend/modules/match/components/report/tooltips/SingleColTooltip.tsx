import { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box } from '@mui/material';
import TooltipDataCol from './TooltipDataCol';
import { DuelStats } from 'modules/match/types/MatchResponses';

interface SingleColTooltipProps {
  stats: DuelStats;
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
