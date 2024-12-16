import React from 'react';
import { Box, Typography, SxProps, Theme } from '@mui/material';

interface TooltipDataItemProps {
  title: string;
  value: number | undefined;
  sx?: SxProps;
}

const TooltipDataItem: React.FC<TooltipDataItemProps> = ({
  title,
  value,
  sx = {},
}) => {
  return (
    <Box
      fontSize={14}
      width={'100%'}
      fontWeight={'bold'}
      color={'#54595E99'}
      display={'flex'}
      textAlign={'center'}
      justifyContent={'space-between'}
      sx={{ ...sx }}>
      <Box
        sx={{
          whiteSpace: 'nowrap',
          textAlign: 'left',
        }}>
        {title}:{' '}
      </Box>
      <Box
        sx={{
          marginLeft: '10px',
          textAlign: 'left',
          fontWeight: 'normal',
          color: '#A4BC10',
        }}>
        {value}
      </Box>
    </Box>
  );
};

export default TooltipDataItem;
