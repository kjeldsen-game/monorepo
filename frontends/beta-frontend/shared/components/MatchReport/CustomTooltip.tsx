import { Action, RStats } from '@/shared/models/MatchReport';
import styled from '@emotion/styled';
import { Box, Grid, Tooltip, tooltipClasses, Typography } from '@mui/material';
import { ReactNode } from 'react';
import ToolTipDataItem from './TooltipDataItem';
import TooltipDataCol from './TooltipDataCol';

interface CustomTooltipProps {
  className?: string;
  children: ReactNode;
  tooltipContent: ReactNode;
  heading?: any;
}

/* <Typography>
{initiatorStats.teamAssistance && challengerStats.teamAssistance ? (
  <Tooltip
    {...props}
    title={
      <Box sx={{ color: 'black' }}>
        <Box sx={{ mt: 2 }}>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              {Object.entries(initiatorStats.teamAssistance).map(
                ([player, value]) => (
                  <Typography key={player}>
                    {player}: {value}
                  </Typography>
                ),
              )}
            </Grid>

            <Grid item xs={6}>
              {Object.entries(challengerStats.teamAssistance).map(
                ([player, value]) => (
                  <Typography key={player}>
                    {player}: {value}
                  </Typography>
                ),
              )}
            </Grid>
          </Grid>
        </Box>
      </Box>
    }
    classes={{ popper: className }}
    arrow>
    <span>variable</span>
  </Tooltip>
) : (
  <></>
)}
</Typography> */

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
