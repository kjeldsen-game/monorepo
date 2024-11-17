import React from 'react';
import { SxProps, Grid, Typography } from '@mui/material';
import { RStats } from '@/shared/models/MatchReport';
import TooltipDataItem from './ToolTipDataItem';

interface TooltipDataColProps {
  stats: RStats;
  sx?: SxProps;
  heading?: string;
}

const TooltipDataCol: React.FC<TooltipDataColProps> = ({
  stats,
  sx = {},
  heading,
}) => {
  return (
    <Grid item xs={6} sx={{ ...sx }} paddingX={'10px'}>
      <Typography fontWeight={'bold'} fontSize={'14px'} textAlign={'center'}>
        {heading}
      </Typography>
      <TooltipDataItem title={'Skill Points'} value={stats.skillPoints} />
      <TooltipDataItem title={'Performance'} value={stats.performance} />

      {stats.assistance != null ? (
        <TooltipDataItem title={'Assistance'} value={stats.assistance} />
      ) : (
        <TooltipDataItem title={'Carryover'} value={stats.carryover} />
      )}
      <TooltipDataItem
        sx={{ borderTop: '1px solid black' }}
        title={'Total'}
        value={stats.total}
      />
    </Grid>
  );
};

export default TooltipDataCol;
