import Link from 'next/link';
import { Box } from '@mui/material';
import React from 'react';

type Variant = 'contained' | 'outlined';

interface SocialMediaLinkProps {
    link: string;
    children: React.ReactNode;
    variant?: Variant;
}

const variantStyleMap: Record<Variant, any> = {
    contained: {
        backgroundColor: '#FF3F84',
        color: '#FFFFFF',
        border: '2px solid transparent',
        '&:hover': {
            backgroundColor: '#FFFFFF',
            color: '#FF3F84',
            borderColor: '#FF3F84',
        },
    },
    outlined: {
        backgroundColor: '#FFFFFF',
        color: '#FF3F84',
        border: '2px solid #FF3F84',
        '&:hover': {
            backgroundColor: '#FF3F84',
            color: '#FFFFFF',
            borderColor: 'transparent',
        },
    },
};

const SocialMediaLink: React.FC<SocialMediaLinkProps> = ({
    link,
    children,
    variant = 'contained',
}) => {
    const variantStyles = variantStyleMap[variant];

    return (
        <Link href={link} passHref>
            <Box
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: 40,
                    height: 40,
                    borderRadius: '50%',
                    transition: 'all 0.3s ease',
                    textDecoration: 'none',
                    cursor: 'pointer',
                    ...variantStyles,
                }}
            >
                {children}
            </Box>
        </Link>
    );
};

export default SocialMediaLink;
