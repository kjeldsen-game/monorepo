import React, { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box, Grid } from '@mui/material';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { PitchArea } from 'modules/player/types/Player';

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
            {Object.values(PitchArea).map((area, index) =>
              area !== PitchArea.OUT_OF_BOUNDS ? (
                <Grid key={index} item xs={4}>
                  <Box
                    sx={{
                      height: '30px',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      outline: '1px solid black',
                      background: area === pitchArea ? '#A4BC10' : '#F8F8F8',
                    }}>
                    {convertSnakeCaseToTitleCase(area)}
                  </Box>
                </Grid>
              ) : null,
            )}
          </Grid>{' '}
        </Box>
      }>
      <span style={{ fontStyle: 'italic' }}>{children}</span>
    </CustomTooltip>
  );
};

export default PitchAreaTooltip;
