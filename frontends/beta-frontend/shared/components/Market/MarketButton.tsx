import React from 'react';
import { Button, SxProps, Theme } from '@mui/material';

interface MarketButtonProps {
    variant?: 'text' | 'outlined' | 'contained';
    sx?: SxProps<Theme>;
    onClick?: React.MouseEventHandler<HTMLButtonElement>;
    children: React.ReactNode;
}

const MarketButton: React.FC<MarketButtonProps> = ({
    variant = 'outlined',
    sx = {},
    onClick,
    children,
    ...rest
}) => {
    const defaultSx: SxProps<Theme> = {
        borderColor: '#FF3F84',
        color: '#FF3F84',
        background: '#FFFFFF',
        '&:hover': {
            backgroundColor: '#FF3F84',
            color: 'white',
            borderColor: 'transparent',
        },
        borderRadius: '4px',
    };

    const mergedSx = { ...defaultSx, ...sx };

    return (
        <Button
            variant={variant}
            sx={mergedSx}
            color="secondary"
            onClick={onClick}
            {...rest}>
            {children}
        </Button>
    );
};

export default MarketButton;
