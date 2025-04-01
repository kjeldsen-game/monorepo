import React from 'react';
import TooltipDataItem from './ToolTipDataItem';
import { Grid, Typography } from '@mui/material';
import { Performance } from '@/shared/models/MatchReport';

interface PerformanceTooltipProps {
  performance: Performance;
}

const PerformanceTooltip: React.FC<PerformanceTooltipProps> = ({
  performance,
}) => {
  return (
    <Grid item xs={6} paddingX={'20px'}>
      <TooltipDataItem title={'Random'} value={performance.random} />
      <TooltipDataItem
        title={'Previous Duel Impact'}
        value={performance.previousTotalImpact}
      />
      <TooltipDataItem
        sx={{ borderTop: '1px solid black' }}
        title={'Total'}
        value={performance.total}
      />
    </Grid>
  );
};

export default PerformanceTooltip;
