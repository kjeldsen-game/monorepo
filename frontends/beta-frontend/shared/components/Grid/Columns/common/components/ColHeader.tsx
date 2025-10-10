import { theme } from '@/libs/material/theme';
import { Box, useMediaQuery } from '@mui/material';
import { GridAlignment } from '@mui/x-data-grid';
import React, { ReactNode, use } from 'react';

interface ColHeaderProps {
  header: string;
  children?: ReactNode;
  align?: GridAlignment
  secondaryHeader?: string;
}

const ColHeader: React.FC<ColHeaderProps> = ({ header, align = "center", children, secondaryHeader }) => {

  const isXs = useMediaQuery(theme.breakpoints.down("sm"))
  const paddingStyle = align
    ? {
      paddingLeft: align === 'left' ? '10px' : undefined,
      paddingRight: align === 'right' ? '10px' : undefined,
    }
    : {};

  return (
    <Box style={paddingStyle}>
      {isXs && secondaryHeader ? <>{secondaryHeader}</> : <>{header}</>}
      {children}
    </Box>
  );
};

export default ColHeader;
