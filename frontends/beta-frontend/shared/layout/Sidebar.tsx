import React, { FC } from 'react'
import Box from '@mui/material/Box'
import Drawer from '@mui/material/Drawer'
import CssBaseline from '@mui/material/CssBaseline'
import Toolbar from '@mui/material/Toolbar'
import List from '@mui/material/List'
import Divider from '@mui/material/Divider'
import ListItem from '@mui/material/ListItem'
import ListItemButton from '@mui/material/ListItemButton'
import ListItemIcon from '@mui/material/ListItemIcon'
import ListItemText from '@mui/material/ListItemText'
import InboxIcon from '@mui/icons-material/MoveToInbox'
import MailIcon from '@mui/icons-material/Mail'
import Link from 'next/link'

const drawerWidth = 240

export interface Item {
  name: string
  to: string
  icon: 'inbox' | 'mail'
  hasDivider?: boolean
  selected: boolean
}

interface SidebarProps {
  items: Item[]
}

export const Sidebar: FC<SidebarProps> = (props) => {
  const providedIcons = { inbox: <InboxIcon />, mail: <MailIcon /> }
  return (
    <Box sx={{ display: 'flex', backgroundColor: 'white' }}>
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: {
            width: drawerWidth,
            boxSizing: 'border-box',
            bgcolor: '#F9F9F9',
          },
        }}>
        <Toolbar />
        <Box
          sx={{
            overflow: 'auto',
          }}>
          <List>
            {props.items.map((item) => (
              <>
                <ListItem
                  key={item.name}
                  disablePadding
                  sx={{
                    backgroundColor: item.selected ? 'white' : 'inherit',
                    borderLeft: item.selected ? '16px solid #FF3F84' : 'inherit',
                    boxShadow: '0 2px white',
                  }}>
                  <Link href={item.to}>
                    <ListItemButton>
                      <ListItemIcon>{providedIcons[item.icon]}</ListItemIcon>
                      <ListItemText primary={item.name} />
                    </ListItemButton>
                  </Link>
                </ListItem>
                {item.hasDivider ? <Divider /> : <></>}
              </>
            ))}
          </List>
        </Box>
      </Drawer>
    </Box>
  )
}
