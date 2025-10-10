import { Avatar, Box, ListItemIcon, ListItemText, Menu, MenuItem, Typography } from '@mui/material';
import { User } from 'next-auth';
import { signOut } from 'next-auth/react';
import React, { FC } from 'react'
import {
    AccountCircle as AccountCircleIcon,
    Logout as LogoutIcon,
} from '@mui/icons-material';
import CustomButton from '../Common/CustomButton';
import { theme } from '@/libs/material/theme';

interface UserMenuProps {
    user?: User;
}

const UserMenu: FC<UserMenuProps> = ({ user }) => {

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
        <Box>
            <CustomButton
                onClick={handleClick}
                sx={{
                    background: '#E6E7EB', color: theme.palette.quaternary.main, fontWeight: 'bold',
                    display: 'flex', justifyContent: 'space-around', padding: 1
                }} fullWidth>
                <Box width={'30%'}>
                    <Avatar src={undefined}></Avatar>
                </Box>
                <Box textAlign={'left'} width={'70%'} marginLeft={1}>
                    <Typography fontWeight={'bold'}>Account</Typography>
                    <Typography variant='subtitle2' fontSize={'10px'}>{user?.email}</Typography>
                </Box>
            </CustomButton>
            <Menu

                sx={{ marginTop: -1, width: '200px' }}
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                slotProps={{
                    paper: {
                        sx: {
                            p: 0,
                            borderRadius: 2,
                            width: '200px',
                            overflowX: "hidden",
                        },
                    },
                }}
                onClose={handleClose}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'center'
                }}
                transformOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center'
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
        </Box>
    );
}

export default UserMenu