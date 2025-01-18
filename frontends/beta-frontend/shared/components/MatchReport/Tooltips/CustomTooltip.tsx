import styled from '@emotion/styled';
import { CloseOutlined } from '@mui/icons-material';
import {
  Box,
  Grid,
  IconButton,
  Tooltip,
  tooltipClasses,
  Typography,
} from '@mui/material';
import { ReactNode, useState } from 'react';

interface CustomTooltipProps {
  className?: string;
  children: ReactNode;
  tooltipContent: ReactNode;
  heading?: any;
  clickOpen?: boolean;
}

const CustomTooltip = styled(
  ({
    className,
    children,
    tooltipContent,
    heading,
    clickOpen = false,
    ...props
  }: CustomTooltipProps) => {
    const [open, setOpen] = useState(false);

    const handleTooltipOpen = () => setOpen(true);
    const handleTooltipClose = () => setOpen(false);

    return (
      <Tooltip
        {...props}
        open={open}
        onClose={handleTooltipClose}
        disableHoverListener
        title={
          <Box
            sx={{ color: 'black' }}
            onMouseEnter={handleTooltipOpen}
            // onMouseLeave={handleTooltipClose}
          >
            <IconButton
              sx={{
                width: '12px',
                height: '12px',
                position: 'absolute',
                left: 'calc(100% - 20px)',
                top: '3%',
                background: '#E5E5E5',
              }}
              onClick={handleTooltipClose}
              aria-label="close">
              <CloseOutlined
                sx={{
                  color: '#4F4F4F',
                  width: '10px',
                  height: '10px',
                }}
              />
            </IconButton>
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
        <span
          onMouseEnter={!clickOpen ? handleTooltipOpen : undefined}
          onClick={clickOpen ? handleTooltipOpen : undefined}
          // onMouseEnter={handleTooltipOpen}
          // onMouseLeave={handleTooltipClose}
        >
          {children}
        </span>
      </Tooltip>
    );
  },
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
