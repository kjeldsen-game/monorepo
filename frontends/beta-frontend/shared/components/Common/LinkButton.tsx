import { SxProps, Box } from '@mui/material';
import { Theme } from '@mui/material/styles';
import Link from 'next/link';
import React from 'react';

interface LinkButtonProps {
  sx?: SxProps<Theme>;
  children: React.ReactNode;
  link: string;
  variant?: 'contained' | 'outlined';
}

const LinkButton: React.FC<LinkButtonProps> = ({
  children,
  link,
  sx,
  variant = 'outlined', // default to outlined
}) => {
  const baseStyles: SxProps<Theme> = {
    padding: '8px 16px',
    textDecoration: 'none',
    textAlign: 'center',
    borderRadius: '4px',
    fontWeight: 500,
    transition: 'all 0.3s ease',
    display: 'inline-block',
  };

  const outlinedStyles: SxProps<Theme> = {
    border: '1px solid #FF3F84',
    color: '#FF3F84',
    '&:hover': {
      backgroundColor: '#FF3F84',
      color: 'white',
      borderColor: 'transparent',
    },
  };

  const containedStyles: SxProps<Theme> = {
    boxShadow: '1',
    backgroundColor: 'white',
    color: '#FF3F84',
    '&:hover': {
      backgroundColor: '#FF3F84',
      color: 'white'
    },
  };

  const variantStyles = variant === 'contained' ? containedStyles : outlinedStyles;

  return (
    <Link href={link} passHref style={{ textDecoration: 'none' }}>
      <Box component="span" sx={{ ...baseStyles, ...variantStyles, ...sx }}>
        {children}
      </Box>
    </Link>
  );
};

export default LinkButton;
