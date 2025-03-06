import Link from 'next/link';
import React from 'react';

interface ColLinkProps {
  urlValue: string;
  children: React.ReactNode;
  sx?: React.CSSProperties;
}

const ColLink: React.FC<ColLinkProps> = ({ children, urlValue, sx }) => {
  return (
    <Link
      style={{
        paddingInline: '20px',
        color: 'black',
        textDecoration: 'none',
        ...sx,
      }}
      passHref
      href={urlValue}>
      {children}
    </Link>
  );
};

export default ColLink;
