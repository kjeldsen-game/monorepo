import { Box } from '@mui/material';
import React from 'react';

interface ProgressBarProps {
  days: number;
}

const ProgressBar = ({ days }: ProgressBarProps) => {
  const NUMBER_OF_CELLS = 14;
  const getColorStyle = (days: number): string => {
    if (days <= 4 && days >= 1) {
      return '#CEDD6F';
    } else if (days <= 11) {
      return '#F3CF72';
    } else {
      return '#F48989';
    }
  };

  const barColor = getColorStyle(days);

  return (
    <Box
      sx={{
        display: 'flex', // Use flexbox to arrange squares in a row
        alignItems: 'center',
        gap: '2px',
      }}>
      <Box sx={{ paddingRight: '5px', fontSize: '20px' }}>
        {String.fromCodePoint('0x1F4AA')}
      </Box>
      {Array.from({ length: NUMBER_OF_CELLS }).map((_, index) => (
        <div
          key={index}
          style={{
            borderRadius: '2px',
            height: '16px',
            width: '8px',
            backgroundColor: index + 1 <= days ? barColor : '#F1F1F1',
          }}></div>
      ))}
    </Box>
  );
};

export default ProgressBar;
