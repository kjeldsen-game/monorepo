import React from 'react';
import Button, { ButtonProps } from '@mui/material/Button';


export type CustomButtonVariants = "outlined" | "contained"

interface CustomButtonProps extends ButtonProps { }

export const variantStylesMap: Record<CustomButtonVariants, object> = {
    contained: {
        backgroundColor: '#FF3F84',
        color: '#FFFFFF',
        border: '1px solid transparent',
        '&:hover': {
            backgroundColor: '#FFFFFF',
            color: '#FF3F84',
            borderColor: '#FF3F84',
        },
    },
    outlined: {
        backgroundColor: '#FFFFFF',
        color: '#FF3F84',
        border: '1px solid #FF3F84',
        '&:hover': {
            backgroundColor: '#FF3F84',
            color: '#FFFFFF',
            borderColor: 'transparent',
        },
    },
};

const CustomButton: React.FC<CustomButtonProps> = ({ variant = 'contained', sx, ...rest }) => {
    const styles = variantStylesMap[variant as CustomButtonVariants] ?? variantStylesMap.outlined;

    return (
        <Button
            variant={variant}
            sx={{
                padding: '2px 4px',
                textAlign: 'center',
                borderRadius: 1,
                transition: 'all 0.3s ease',
                textDecoration: 'none',
                cursor: 'pointer',
                ...styles,
                ...sx,
            }}
            {...rest}
        />
    );
};

export default CustomButton;
