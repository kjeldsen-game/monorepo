import { GridAlignment } from '@mui/x-data-grid';
import React from 'react';

interface ColHeaderProps {
  header: string;
  align?: GridAlignment
}

const ColHeader: React.FC<ColHeaderProps> = ({ header, align = "center" }) => {
  const paddingStyle = align
    ? {
      paddingLeft: align === 'left' ? '10px' : undefined,
      paddingRight: align === 'right' ? '10px' : undefined,
      // border: '1px solid red'
    }
    : {};

  return <div style={paddingStyle}>{header}</div>;
};

export default ColHeader;
