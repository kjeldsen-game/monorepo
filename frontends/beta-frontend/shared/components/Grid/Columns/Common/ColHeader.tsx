import React from 'react';

interface ColHeaderProps {
  header: string;
  align?: 'left' | 'right';
}

const ColHeader: React.FC<ColHeaderProps> = ({ header, align }) => {
  const paddingStyle = align
    ? {
        paddingLeft: align === 'left' ? '20px' : undefined,
        paddingRight: align === 'right' ? '20px' : undefined,
      }
    : {};

  return <div style={paddingStyle}>{header}</div>;
};

export default ColHeader;
