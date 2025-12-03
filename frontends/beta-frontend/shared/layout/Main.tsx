import { FC, PropsWithChildren } from 'react';
import { Box } from '@mui/material';

export const Main: FC<PropsWithChildren> = ({ children }) => {
    return (
        <Box sx={{padding: {xs: 1, sm: 2,}}}>
            {children}
        </Box>
    );
};
