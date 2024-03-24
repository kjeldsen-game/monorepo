import React, { FC } from 'react'
import { AppBar, Box, Button, ListItemIcon, ListItemText, Menu, MenuItem, Toolbar, Typography } from '@mui/material'
import { AccountCircle as AccountCircleIcon, Login as LoginIcon, Logout as LogoutIcon } from '@mui/icons-material'
import { signOut, useSession } from 'next-auth/react'
import Link from 'next/link'
import { User } from 'next-auth'
import SportsSoccerIcon from '@mui/icons-material/SportsSoccer'

interface SignedUserProps {
  user?: User
}

const SignedUser: FC<SignedUserProps> = ({ user }) => {
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null)
  const open = Boolean(anchorEl)
  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget)
  }
  const handleClose = () => {
    setAnchorEl(null)
  }

  const handleLogout = async () => {
    await signOut()
  }

  return (
    <>
      <Button
        aria-controls={open ? 'signed-user-menu' : undefined}
        aria-haspopup="true"
        aria-expanded={open ? 'true' : undefined}
        variant="outlined"
        color="primary"
        onClick={handleClick}>
        {user?.name || 'Anonymous'}
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
  )
}

export const Header: FC = () => {
  const { status, data } = useSession()

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar
        position="fixed"
        sx={{
          boxShadow: '0px 0px 0px 0px',
          borderBottom: '4px solid #FF3F84',
          // height: SportsSoccerIcon.length * 4,
          zIndex: (theme) => theme.zIndex.drawer + 1,
          backgroundColor: 'white',
        }}>
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            <Link
              href="/"
              style={{
                width: 'min-content',
                color: 'black',
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
              }}>
              <SportsSoccerIcon />
              <span>Kjeldsen</span>
            </Link>
          </Typography>

          {status === 'authenticated' && !!data ? (
            <SignedUser user={data.user} />
          ) : (
            <Link href="/signin" passHref>
              <Button startIcon={<LoginIcon />} variant="outlined" color="primary">
                Sign In
              </Button>
            </Link>
          )}
        </Toolbar>
      </AppBar>
    </Box>
  )
}
