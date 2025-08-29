import { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box } from '@mui/material';
import TooltipDataItem from './items/ToolTipDataItem';
import { Stats } from '@/shared/models/match/MatchReport';

interface PlayerStatsTooltipProps {
  stats: Stats;
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
              title={'Goals'}
              value={stats?.goals}
            />
            <TooltipDataItem
              sx={{ color: '#FF3F84' }}
              title={'Missed'}
              value={stats?.missed}
            />
            <TooltipDataItem
              sx={{ color: '#FF3F84' }}
              title={'Total Shots'}
              value={stats?.shots}
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
