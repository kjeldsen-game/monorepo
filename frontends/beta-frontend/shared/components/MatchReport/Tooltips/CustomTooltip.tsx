import styled from '@emotion/styled';
import { Box, Grid, Tooltip, tooltipClasses, Typography } from '@mui/material';
import { ReactNode } from 'react';

interface CustomTooltipProps {
  className?: string;
  children: ReactNode;
  tooltipContent: ReactNode;
  heading?: any;
}

const CustomTooltip = styled(
  ({
    className,
    children,
    tooltipContent,
    heading,
    ...props
  }: CustomTooltipProps) => (
    <Tooltip
      {...props}
      title={
        <Box sx={{ color: 'black' }}>
          <Typography
            fontSize={'14px'}
            fontWeight={'bold'}
            paddingBottom={'5px'}
            textAlign={'center'}>
            {heading}
          </Typography>
          {tooltipContent}
        </Box>
      }
      classes={{ popper: className }}
      arrow>
      <span>{children}</span>
    </Tooltip>
  ),
)({
  [`& .${tooltipClasses.tooltip}`]: {
    maxWidth: 500,
    backgroundColor: 'white',
    border: '2px solid #FF3F84',
    borderRadius: '8px',
    padding: '10px',
  },
  [`&.${tooltipClasses.popper}[data-popper-placement*="bottom"] .${tooltipClasses.tooltip}`]:
    {
      marginTop: '6px',
    },
  [`& .${tooltipClasses.arrow}`]: {
    color: '#FF3F84',
    width: '16px',
    height: '12px',
    top: '-6px !important',
  },
});

export default CustomTooltip;
