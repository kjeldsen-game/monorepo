import React, { ReactNode } from 'react';
import CustomTooltip from '../CustomTooltip';
import { Box } from '@mui/material';
import TooltipDataCol from '../TooltipDataCol';
import { DENOMINATIONS_RANGES, RStats } from '@/shared/models/MatchReport';
import { getRangeLabel } from '../MatchReportItemMessage';
import QualityText from '../MatchReportMessages/QualityText';

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
