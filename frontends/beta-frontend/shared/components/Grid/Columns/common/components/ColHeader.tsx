import { GridAlignment } from '@mui/x-data-grid';
import React, { ReactNode } from 'react';

interface ColHeaderProps {
  header: string;
  children?: ReactNode;
  align?: GridAlignment
}

const ColHeader: React.FC<ColHeaderProps> = ({ header, align = "center", children }) => {
  const paddingStyle = align
    ? {
      paddingLeft: align === 'left' ? '10px' : undefined,
      paddingRight: align === 'right' ? '10px' : undefined,
      // border: '1px solid red'
    }
    : {};

  return <div style={paddingStyle}>{header}{children}</div>;
};

export default ColHeader;
