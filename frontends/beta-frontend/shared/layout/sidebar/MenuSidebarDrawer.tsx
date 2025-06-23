import { Divider, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Toolbar } from '@mui/material'
import { useRouter } from 'next/router';
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents';
import Groups2Icon from '@mui/icons-material/Groups2';
import LocalGroceryStoreIcon from '@mui/icons-material/LocalGroceryStore';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import TerminalIcon from '@mui/icons-material/Terminal';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import Link from 'next/link';

export const MENU_SIDEBAR_ICONS = {
    inbox: <InboxIcon />,
    mail: <MailIcon />,
    trophy: <EmojiEventsIcon />,
    market: <LocalGroceryStoreIcon />,
    team: <Groups2Icon />,
    training: <FitnessCenterIcon />,
    league: <CalendarMonthIcon />,
    simulator: <TerminalIcon />,
};

export interface Item {
    name: string;
    to: string;
    icon: keyof typeof MENU_SIDEBAR_ICONS;
    hasDivider?: boolean;
    selected: boolean;
    beta?: boolean,
}

const MenuSidebarDrawer = () => {

    const { pathname } = useRouter();

    const items: Item[] = [
        {
            name: 'Dashboard',
            icon: 'inbox',
            to: '/dashboard',
            selected: pathname === '/dashboard',
        },
        {
            name: 'Team',
            icon: 'team',
            to: '/team',
            selected: pathname === '/team',
        },
        {
            name: 'Economy',
            icon: 'market',
            to: '/team/economy',
            hasDivider: false,
            selected: pathname === '/team/economy',
            beta: true
        },
        // {
        //   name: 'Training',
        //   icon: 'training',
        //   to: '/training',
        //   hasDivider: false,
        //   selected: pathname === '/training',
        // },
        {
            name: 'Challenge',
            icon: 'trophy',
            to: '/challenge',
            hasDivider: false,
            selected: /^\/challenge/.test(pathname),
        },
        // {
        //   name: 'Market',
        //   icon: 'market',
        //   to: '/market',
        //   hasDivider: false,
        //   selected: /^\/market/.test(pathname),
        // },
        {
            name: 'League',
            icon: 'league',
            to: '/league',
            hasDivider: false,
            selected: /^\/league/.test(pathname),
        },
        {
            name: 'Simulator',
            icon: 'simulator',
            to: '/simulator',
            hasDivider: false,
            selected: /^\/simulator/.test(pathname),
        },
    ];

    return (
        <div>
            <Toolbar />
            <List>
                {items.map((item) => (
                    <ListItem
                        key={item.name}
                        disablePadding
                        sx={{
                            color: item.selected ? ' #FF3F84' : 'inherit',
                            backgroundColor: item.selected ? 'white' : 'inherit',
                            borderLeft: item.selected ? '8px solid #FF3F84' : 'inherit',
                            boxShadow: '0 2px white',
                        }}>
                        <Link
                            href={item.to}
                            style={{
                                width: '100%',
                                textDecoration: 'none',
                                color: 'black',
                            }}>
                            <ListItemButton>
                                <ListItemIcon>{MENU_SIDEBAR_ICONS[item.icon]}</ListItemIcon>
                                <ListItemText>
                                    {item.name}
                                    {item.beta ? <sup style={{ color: '#FF3F84', paddingLeft: '4px' }}>Beta</sup> : <></>}
                                </ListItemText>
                            </ListItemButton>
                        </Link>
                        {item.hasDivider ? <Divider /> : <></>}
                    </ListItem>
                ))}
            </List>
        </div>
    )
}

export default MenuSidebarDrawer