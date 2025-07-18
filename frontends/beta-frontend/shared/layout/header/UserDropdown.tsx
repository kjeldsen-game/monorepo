import { Button, ListItemIcon, ListItemText, Menu, MenuItem } from '@mui/material';
import { User } from 'next-auth';
import { signOut } from 'next-auth/react';
import React, { FC } from 'react'
import {
    AccountCircle as AccountCircleIcon,
    Logout as LogoutIcon,
} from '@mui/icons-material';
import MarketButton from '@/shared/components/Market/MarketButton';

interface UserDropdownProps {
    user?: User;
}

const UserDropdown: FC<UserDropdownProps> = ({ user }) => {

    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleLogout = async () => {
        await signOut();
    };

    return (
        <>
            <MarketButton
                aria-controls={open ? 'signed-user-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                variant="outlined"
                onClick={handleClick}>
                {user?.email ?? 'Anonymous'}
            </MarketButton>
            <Menu
                sx={{ marginTop: 1 }}
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'signed-user-button',
                }}>
                <MenuItem onClick={() => window.location.href = '/profile'}>
                    <ListItemIcon>
                        <AccountCircleIcon fontSize="small" />
                    </ListItemIcon>
                    <ListItemText>Profile</ListItemText>
                </MenuItem>
                <MenuItem onClick={handleLogout}>
                    <ListItemIcon>
                        <LogoutIcon fontSize="small" />
                    </ListItemIcon>
                    <ListItemText>Sign Out</ListItemText>
                </MenuItem>
            </Menu>
        </>
    );
}

export default UserDropdown