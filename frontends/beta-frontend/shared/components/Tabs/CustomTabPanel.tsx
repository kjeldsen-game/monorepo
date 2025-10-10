import { Box, Fade } from '@mui/material';
import { CSSProperties } from 'react';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
  sx?: CSSProperties;
}

function CustomTabPanel(props: TabPanelProps) {
  const { children, value, index, sx, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
      style={{ ...sx }}
    >
      <Fade in={value === index} timeout={300}>
        <Box sx={{ py: 0 }}>
          {children}
        </Box>
      </Fade>
    </div>
  );
}

export { CustomTabPanel };
