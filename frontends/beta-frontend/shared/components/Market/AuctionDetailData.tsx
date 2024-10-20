import React from 'react';
import { Box, Typography, SxProps, Theme } from '@mui/material';

interface DataBoxProps {
    title: string;
    value: React.ReactNode;
    sx?: SxProps<Theme>;
}

const AuctionDetailData: React.FC<DataBoxProps> = ({ title, value, sx }) => {
    return (
        <Box sx={{ flex: 1, textAlign: 'center' }}>
            <Typography variant="body2" sx={{ color: '#54595E99' }}>
                {title}
            </Typography>
            <Typography
                variant="body2"
                sx={{ color: '#A4BC10', padding: '4px', ...sx }}>
                {value}
            </Typography>
        </Box>
    );
};

export default AuctionDetailData;
