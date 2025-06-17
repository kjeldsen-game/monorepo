import { Button, ListItemIcon, ListItemText, Menu, MenuItem } from '@mui/material';
import { User } from 'next-auth';
import { signOut } from 'next-auth/react';
import React, { FC } from 'react'
import {
    AccountCircle as AccountCircleIcon,
    Logout as LogoutIcon,
} from '@mui/icons-material';

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
            <Button
                aria-controls={open ? 'signed-user-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                variant="outlined"
                color="primary"
                onClick={handleClick}>
                {user?.email ?? 'Anonymous'}
            </Button>
            <Menu
                sx={{ marginTop: 1 }}
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'signed-user-button',
                }}>
                <MenuItem onClick={() => undefined}>
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