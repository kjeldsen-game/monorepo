import { IconButtonProps, SxProps } from '@mui/material';
import { Theme } from '@mui/material/styles';
import React from 'react';
import { useRouter } from 'next/router';
import CustomIconButton from './CustomIconButton';

interface IconLinkButtonProps extends IconButtonProps {
    sx?: SxProps<Theme>;
    children: React.ReactNode;
    link: string;
    variant?: 'contained' | 'outlined';
}

const IconLinkButton: React.FC<IconLinkButtonProps> = ({
    children,
    link,
    variant = 'contained'
}) => {
    const router = useRouter();

    return (
        <CustomIconButton variant={variant} onClick={() => router.push(link)}>
            {children}
        </CustomIconButton>
    );
};

export default IconLinkButton;
