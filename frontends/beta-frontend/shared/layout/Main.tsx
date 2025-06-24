import { FC, PropsWithChildren } from 'react';
import { Box } from '@mui/material';

export const Main: FC<PropsWithChildren> = ({ children }) => {
    return (
        <Box>
            {children}
        </Box>
    );
};
