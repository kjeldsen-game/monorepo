// components/Common/CustomIconButton.tsx
import React from 'react';
import IconButton, { IconButtonProps } from '@mui/material/IconButton';
import { SxProps, Theme } from '@mui/material';
import { variantStylesMap } from './CustomButton';

export type CustomIconButtonVariants = "outlined" | "contained"

interface CustomIconButtonProps extends IconButtonProps {
    variant?: CustomIconButtonVariants;
    sx?: SxProps<Theme>;
}

const CustomIconButton: React.FC<CustomIconButtonProps> = ({
    variant = 'contained',
    sx,
    ...rest
}) => {
    const styles = variantStylesMap[variant];

    return (
        <IconButton
            sx={{
                padding: '2px',
                borderRadius: 1,
                transition: 'all 0.3s ease',
                ...styles,
                ...sx,
            }}
            {...rest}
        />
    );
};

export default CustomIconButton;
