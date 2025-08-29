import { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box, Typography } from '@mui/material';
import TooltipDataItem from './items/ToolTipDataItem';
import { Stats } from '@/shared/models/match/MatchReport';

interface DisruptionTooltipProps {
  random: number;
  difference: number;
  total: number;
  children: ReactNode;
}

const DisruptionTooltip = ({
  random,
  difference,
  total,
  children,
}: DisruptionTooltipProps) => {
  return (
    <CustomTooltip
      tooltipContent={
        <Box sx={{ color: 'black' }}>
          <Box>
            <TooltipDataItem title={'Total'} value={total} />
            <TooltipDataItem title={'Random'} value={random} />
            <CustomTooltip
              tooltipContent={
                <Box sx={{ color: 'black' }}>
                  <Box> 100 - {total} / 2</Box>
                </Box>
              }>
              <TooltipDataItem
                sx={{
                  borderTop: '1px solid black',
                  color: '#FF3F84',
                }}
                title={'Difference'}
                value={difference}
              />
            </CustomTooltip>
          </Box>
        </Box>
      }>
      {children}
    </CustomTooltip>
  );
};

export default DisruptionTooltip;
