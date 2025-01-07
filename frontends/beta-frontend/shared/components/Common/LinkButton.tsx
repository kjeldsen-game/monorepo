import { SxProps, Box } from '@mui/material';
import { Theme } from 'next-auth';
import Link from 'next/link';
import React from 'react';

interface LinkButtonProps {
  sx?: SxProps<Theme>;
  children: React.ReactNode;
  link: string;
}

const LinkButton: React.FC<LinkButtonProps> = ({ children, link, sx }) => {
  const defaultSx: SxProps<Theme> = {
    padding: '8px',
    textDecoration: 'none',
    border: '1px solid #FF3F84',
    color: '#FF3F84',
    '&:hover': {
      backgroundColor: '#FF3F84',
      color: 'white',
      borderColor: 'transparent',
    },
    borderRadius: '4px',
    ...sx, // Allow for custom styles to be passed in via the `sx` prop
  };

  return (
    <Link style={{ textDecoration: 'none' }} passHref href={link}>
      <Box sx={defaultSx}>{children}</Box>
    </Link>
  );
};

export default LinkButton;
