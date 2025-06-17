import * as React from 'react';
import Box from '@mui/material/Box';
import CssBaseline from '@mui/material/CssBaseline';
import Toolbar from '@mui/material/Toolbar';
import { Main } from './Main';

import MenuSidebar, { DRAWER_WIDTH } from './sidebar/MenuSidebar';
import Header from './header/Header';
import Subheader from './header/Subheader';
import HeaderDivider from './header/HeaderDivider';

interface Props {
    children: React.ReactNode;
}

export const Layout: React.FC<React.PropsWithChildren<Props>> = ({ children }) => {

    const [mobileOpen, setMobileOpen] = React.useState(false);
    const [isClosing, setIsClosing] = React.useState(false);

    return (
        <Box sx={{ display: 'flex' }}>
            <CssBaseline />
            <Header
                isClosing={isClosing}
                mobileOpen={mobileOpen}
                setMobileOpen={setMobileOpen}
            />
            <HeaderDivider />
            <Subheader />
            <MenuSidebar
                mobileOpen={mobileOpen}
                setMobileOpen={setMobileOpen}
                setIsClosing={setIsClosing}
            />
            <Box
                component="main"
                sx={{
                    mt: { xs: '56px', sm: '47px' },
                    flexGrow: 1,
                    p: 3,
                    width: { sm: `calc(100% - ${DRAWER_WIDTH}px)` },
                }}>
                <Toolbar />
                <Main>{children}</Main>
            </Box>
        </Box>
    );
};