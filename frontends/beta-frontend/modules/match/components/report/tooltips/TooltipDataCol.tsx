import React from 'react';
import { SxProps, Grid, Typography } from '@mui/material';
import { RStats } from '@/shared/models/MatchReport';
import TooltipDataItem from './items/ToolTipDataItem';
import CustomTooltip from './CustomTooltip';
import PerformanceTooltip from './PerformanceTooltip';
import { DuelStats } from '@/shared/models/match/Play';

interface TooltipDataColProps {
  stats: DuelStats;
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
    <Grid size={6} sx={{ ...sx }} paddingX={'20px'}>
      <Typography fontWeight={'bold'} fontSize={'14px'} textAlign={'center'}>
        {heading}
      </Typography>
      <TooltipDataItem title={'Skill Points'} value={stats.skillPoints} />
      <CustomTooltip
        tooltipContent={<PerformanceTooltip performance={stats.performance} />}>
        <TooltipDataItem
          sx={{ color: '#FF3F84' }}
          title={'Random'}
          value={stats.performance.total}
        />
      </CustomTooltip>

      {showAll ? (
        <>
          {stats.assistance != null ? (
            <TooltipDataItem
              title={'Assistance'}
              value={stats.assistance.total}
            />
          ) : null}
          {stats.carryover != null ? (
            <TooltipDataItem title={'Carryover'} value={stats.carryover} />
          ) : null}
        </>
      ) : stats.carryover != null ? (
        <TooltipDataItem title={'Carryover'} value={stats.carryover} />
      ) : null}

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
          value={
            stats.skillPoints + stats.performance.total + (stats.carryover || 0)
          }
        />
      )}
    </Grid>
  );
};

export default TooltipDataCol;
