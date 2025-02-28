import React from 'react';
import { SxProps, Grid, Typography } from '@mui/material';
import { RStats } from '@/shared/models/MatchReport';
import TooltipDataItem from './ToolTipDataItem';

interface TooltipDataColProps {
  stats: RStats;
  sx?: SxProps;
  heading?: string;
  showAll?: boolean;
}

const TooltipDataCol: React.FC<TooltipDataColProps> = ({
  stats,
  sx = {},
  heading,
  showAll = false,
}) => {
  return (
    <Grid item xs={6} sx={{ ...sx }} paddingX={'20px'}>
      <Typography fontWeight={'bold'} fontSize={'14px'} textAlign={'center'}>
        {heading}
      </Typography>
      <TooltipDataItem title={'Skill Points'} value={stats.skillPoints} />
      <TooltipDataItem title={'Performance'} value={stats.performance} />

      {showAll ? (
        <>
          {stats.assistance != null ? (
            <TooltipDataItem title={'Assistance'} value={stats.assistance} />
          ) : null}
          <TooltipDataItem title={'Carryover'} value={stats.carryover || 0} />
        </>
      ) : (
        <TooltipDataItem title={'Carryover'} value={stats.carryover || 0} />
      )}

      {showAll ? (
        <>
          <TooltipDataItem
            sx={{ borderTop: '1px solid black' }}
            title={'Total'}
            value={stats.total}
          />
        </>
      ) : (
        <TooltipDataItem
          sx={{ borderTop: '1px solid black' }}
          title={'Total'}
          value={stats.skillPoints + stats.performance}
        />
      )}
    </Grid>
  );
};

export default TooltipDataCol;
