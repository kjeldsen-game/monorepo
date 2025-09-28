import { Drawer } from '@mui/material'
import { Box } from '@mui/system'
import { FC } from 'react'
import MenuSidebarDrawer from './MenuSidebarDrawer'

export const DRAWER_WIDTH = 200;

interface MenuSidebarProps {
    mobileOpen: boolean;
    setMobileOpen: (value: boolean) => void;
    setIsClosing: (value: boolean) => void;
    window?: () => Window;
}

const MenuSidebar: FC<MenuSidebarProps> = ({ mobileOpen, setMobileOpen, setIsClosing, window }) => {

    const container =
        window !== undefined ? () => window().document.body : undefined;


    const handleDrawerClose = () => {
        setIsClosing(true);
        setMobileOpen(false);
    };

    const handleDrawerTransitionEnd = () => {
        setIsClosing(false);
    };

    return (
        <Box
            component="nav"
            sx={{ width: { sm: DRAWER_WIDTH }, flexShrink: { sm: 0 } }}
            aria-label="mailbox folders">
            <Drawer
                container={container}
                variant="temporary"
                open={mobileOpen}
                onTransitionEnd={handleDrawerTransitionEnd}
                onClose={handleDrawerClose}
                sx={{
                    display: { xs: 'block', sm: 'none' },
                    '& .MuiDrawer-paper': {
                        boxSizing: 'border-box',
                        width: DRAWER_WIDTH,
                        // background: 'white'
                        background: '#F9F9F9',
                        mt: '30px',
                    },
                }}
                slotProps={{
                    root: {
                        keepMounted: true,
                    },
                }}>
                <MenuSidebarDrawer />
            </Drawer>
            <Drawer
                variant="permanent"
                sx={{
                    display: { xs: 'none', sm: 'block' },
                    '& .MuiDrawer-paper': {
                        boxSizing: 'border-box',
                        width: DRAWER_WIDTH,
                        background: 'white',
                        mt: '30px',
                    },
                }}
                open>
                <MenuSidebarDrawer />
            </Drawer>
        </Box>
    )
}

export default MenuSidebar