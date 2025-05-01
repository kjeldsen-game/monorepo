import { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box } from '@mui/material';
import TooltipDataCol from './TooltipDataCol';
import { RStats } from '@/shared/models/MatchReport';
import TooltipDataItem from './ToolTipDataItem';

interface PlayerStatsTooltipProps {
  stats: any;
  children: ReactNode;
}

const PlayerStatsTooltip = ({ stats, children }: PlayerStatsTooltipProps) => {
  return (
    <CustomTooltip
      tooltipContent={
        <Box sx={{ color: 'black' }}>
          <Box>
            <TooltipDataItem
              sx={{ color: '#FF3F84' }}
              title={'Score'}
              value={stats?.score}
            />
            <TooltipDataItem
              sx={{ color: '#FF3F84' }}
              title={'Missed'}
              value={stats?.missed}
            />
            <TooltipDataItem
              sx={{ color: '#FF3F84' }}
              title={'Passes'}
              value={stats?.passes}
            />
            <TooltipDataItem
              sx={{ color: '#FF3F84' }}
              title={'Missed Passes'}
              value={stats?.failedPasses}
            />
            <TooltipDataItem
              sx={{ color: '#FF3F84' }}
              title={'Tackles'}
              value={stats?.tackles}
            />
            <TooltipDataItem
              sx={{ color: '#FF3F84' }}
              title={'Saved'}
              value={stats?.saved}
            />
            {/* <TooltipDataCol stats={stats} /> */}
          </Box>
        </Box>
      }>
      {children}
    </CustomTooltip>
  );
};

export default PlayerStatsTooltip;
