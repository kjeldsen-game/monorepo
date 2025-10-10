import { theme } from '@/libs/material/theme';
import { Box, Typography, TypographyProps } from '@mui/material';
import React, { ReactNode } from 'react'

interface CustomCardHeaderProps extends TypographyProps {
    children: ReactNode;
}

const CustomCardHeader: React.FC<CustomCardHeaderProps> = ({ children, sx }) => {
    return (
        <Box>
            <Typography fontWeight={'bold'} sx={{ color: theme.palette.quaternary.main, ...sx }}>
                {children}
            </Typography>
        </Box>
    )
}

export default CustomCardHeader