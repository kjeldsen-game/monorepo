import React, { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box, Grid } from '@mui/material';
import { PITCH_AREAS, PitchArea } from '@/shared/models/PitchArea';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';

interface PitchAreaTooltipProps {
  children: ReactNode;
  pitchArea: String;
}

const PitchAreaTooltip: React.FC<PitchAreaTooltipProps> = ({
  children,
  pitchArea,
}) => {
  return (
    <CustomTooltip
      tooltipContent={
        <Box sx={{ color: 'black' }}>
          <Grid container width={'300px'}>
            {PITCH_AREAS.map((area, index) => (
              <Grid key={index} item xs={4}>
                <Box
                  sx={{
                    height: '30px',
                    // border: '1px solid black',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    outline: '1px solid black',
                    background: area === pitchArea ? '#A4BC10' : '#F8F8F8',
                  }}>
                  {convertSnakeCaseToTitleCase(area)}
                </Box>
              </Grid>
            ))}
          </Grid>{' '}
        </Box>
      }>
      {children}
    </CustomTooltip>
  );
};

export default PitchAreaTooltip;
