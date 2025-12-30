import { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box } from '@mui/material';
import TooltipDataCol from './TooltipDataCol';
import { DuelStats } from 'modules/match/types/MatchResponses';

interface SingleColTooltipProps {
  stats: DuelStats;
  children: ReactNode;
  showAll?: boolean;
}

const SingleColTooltip = ({ stats, children, showAll = false }: SingleColTooltipProps) => {
  return (
    <CustomTooltip
      tooltipContent={
        <Box sx={{ color: 'black' }}>
          <Box>
            <TooltipDataCol stats={stats} showAll={showAll} />
          </Box>
        </Box>
      }>
      {children}
    </CustomTooltip>
  );
};

export default SingleColTooltip;
