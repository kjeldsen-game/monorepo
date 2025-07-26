import { SxProps } from '@mui/material';
import { Theme } from '@mui/material/styles';
import React from 'react';
import CustomButton from './CustomButton';
import { useRouter } from 'next/router';

interface LinkButtonProps {
  sx?: SxProps<Theme>;
  children: React.ReactNode;
  link: string;
  variant?: 'contained' | 'outlined';
}

const LinkButton: React.FC<LinkButtonProps> = ({
  children,
  link,
}) => {
  const router = useRouter();

  return (
    <CustomButton variant='outlined' onClick={() => router.push(link)}>
      {children}
    </CustomButton>
  );
};

export default LinkButton;
