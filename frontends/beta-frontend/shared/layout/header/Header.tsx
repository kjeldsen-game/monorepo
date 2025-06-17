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
import UserDropdown from './UserDropdown';

interface HeaderProps {
    isClosing: boolean;
    mobileOpen: boolean;
    setMobileOpen: (value: boolean) => void;
}

const Header: FC<HeaderProps> = ({ isClosing, setMobileOpen, mobileOpen }) => {
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
                <IconButton
                    data-testid="menu-icon"
                    color="inherit"
                    aria-label="open drawer"
                    edge="start"
                    onClick={handleDrawerToggle}
                    sx={{ mr: 2, display: { sm: 'none' } }}>
                    <MenuIcon />
                </IconButton>
                <Box display="flex" alignItems="center" width="100%">
                    <Box flex={1} />
                    <Box>
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
                    <Box
                        flex={1}
                        display="flex"
                        justifyContent="flex-end"
                        alignItems="center">
                        {status === 'authenticated' && !!data.user ? (
                            <UserDropdown user={data.user} />
                        ) : (
                            <Link href="/signin" passHref data-testid="signin-button">
                                <Button
                                    startIcon={<LoginIcon />}
                                    variant="outlined"
                                    color="primary">
                                    Sign In
                                </Button>
                            </Link>
                        )}
                    </Box>
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default Header;
