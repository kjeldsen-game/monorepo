import {
    AppBar,
    Box,
    Button,
    IconButton,
    Toolbar,
    Typography,
} from '@mui/material';
import { FC } from 'react';
import MenuIcon from '@mui/icons-material/Menu';
import Link from 'next/link';
import Image from 'next/image';
import { useSession } from 'next-auth/react';
import {
    Login as LoginIcon,
} from '@mui/icons-material';
import CustomButton from '@/shared/components/Common/CustomButton';
import NotificationMenu from '@/shared/components/notification-menu/NotificationMenu';

interface HeaderProps {
    isClosing: boolean;
    mobileOpen: boolean;
    setMobileOpen: (value: boolean) => void;
    isMenu?: boolean;
}

const Header: FC<HeaderProps> = ({ isClosing, setMobileOpen, mobileOpen, isMenu = true }) => {
    const { status, data } = useSession();

    const handleDrawerToggle = () => {
        if (!isClosing) {
            setMobileOpen(!mobileOpen);
        }
    };

    return (
        <AppBar
            position="fixed"
            sx={{
                zIndex: (theme) => theme.zIndex.drawer + 1,
                width: '100%',
                background: 'white',
                color: 'black',
            }}>
            <Toolbar>
                {isMenu &&
                    <IconButton
                        data-testid="menu-icon"
                        color="inherit"
                        aria-label="open drawer"
                        edge="start"
                        onClick={handleDrawerToggle}
                        sx={{ mr: 2, display: { sm: 'none' } }}>
                        <MenuIcon />
                    </IconButton>
                }
                <Box display="flex" alignItems="center" width="100%">
                    <Box flex={1}>
                        {/* Left side (menu button already handled above) */}
                    </Box>

                    <Box
                        sx={{
                            flex: '0 0 auto',
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                        }}>
                        <Link
                            href="/"
                            style={{
                                width: 'min-content',
                                color: 'black',
                                display: 'flex',
                                alignItems: 'center',
                                gap: '8px',
                            }}>
                            <Image
                                width={32}
                                height={32}
                                src="/img/Kjeldsen.png"
                                alt="Kjeldsen Logo"
                            />
                        </Link>
                    </Box>

                    <Box flex={1} display="flex" justifyContent="flex-end" alignItems="center">
                        {isMenu && (
                            status === 'authenticated' && !!data.user && (
                                <>
                                    <NotificationMenu />
                                </>
                            )
                        )}
                    </Box>
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default Header;
