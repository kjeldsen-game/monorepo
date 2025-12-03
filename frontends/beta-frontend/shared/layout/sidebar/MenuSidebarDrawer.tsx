import { Box, Divider, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Toolbar } from '@mui/material'
import { useRouter } from 'next/router';
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents';
import Groups2Icon from '@mui/icons-material/Groups2';
import LocalGroceryStoreIcon from '@mui/icons-material/LocalGroceryStore';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import TerminalIcon from '@mui/icons-material/Terminal';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import Link from 'next/link';
import CustomButton from '@/shared/components/Common/CustomButton';
import { LoginOutlined } from '@mui/icons-material';
import { theme } from '@/libs/material/theme';
import { useSession } from 'next-auth/react';
import UserMenu from '@/shared/components/user-menu/UserMenu';
import { t } from 'i18next';

export const MENU_SIDEBAR_ICONS = {
    inbox: <InboxIcon />,
    mail: <MailIcon />,
    trophy: <EmojiEventsIcon />,
    market: <LocalGroceryStoreIcon />,
    team: <Groups2Icon />,
    training: <FitnessCenterIcon />,
    league: <CalendarMonthIcon />,
    simulator: <TerminalIcon />,
    economy: <AccountBalanceIcon />
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
    const { status, data } = useSession();
    const { pathname } = useRouter();

    const sections = [
        {
            items: [
                {
                    name: 'Dashboard',
                    icon: 'inbox',
                    to: '/dashboard',
                    selected: pathname === '/dashboard',
                },
            ]
        },
        {
            label: "TEAM MANAGEMENT",
            items: [
                {
                    name: 'Team',
                    icon: 'team',
                    to: '/team',
                    selected: pathname === '/team',
                },
                {
                    name: 'Training',
                    icon: 'training',
                    to: '/training',
                    selected: pathname === '/training',
                    beta: true
                }
            ]
        },
        {
            label: "CLUB MANAGEMENT",
            items: [
                {
                    name: 'Economy',
                    icon: 'economy',
                    to: '/team/economy',
                    selected: pathname === '/team/economy',
                    beta: true
                },
                {
                    name: 'Market',
                    icon: 'market',
                    to: '/market',
                    selected: /^\/market/.test(pathname),
                    beta: true
                }
            ]
        },
        {
            label: "COMPETITIONS",
            items: [
                {
                    name: 'Challenge',
                    icon: 'trophy',
                    to: '/challenge',
                    selected: /^\/challenge/.test(pathname)
                },
                {
                    name: 'League',
                    icon: 'league',
                    to: '/league',
                    selected: /^\/league/.test(pathname)
                },

            ]
        },
        {
            label: "ADMIN",
            items: [
                {
                    name: 'Simulator',
                    icon: 'simulator',
                    to: '/simulator',
                    selected: /^\/simulator/.test(pathname)
                }
            ]
        },
    ];
    return (
        <div style={{ height: 'calc(100% - 30px)' }}>
            <Toolbar />
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    height: 'calc(100% - 64px)',
                }}
            >
                <List sx={{ flexGrow: 1, padding: 1 }}>
                    {sections.map((section) => (
                        <Box key={section.label} sx={{ mt: 1 }}>
                            <Box sx={{
                                px: 1,
                                fontSize: '12px',
                                color: theme.palette.quaternary.main,
                                fontWeight: '600'
                            }}>
                                {section.label}
                            </Box>

                            {section.items.map((item) => (
                                <ListItem
                                    key={item.name}
                                    disablePadding
                                    sx={{
                                        borderRadius: 2,
                                        backgroundColor: item.selected ? 'white' : 'inherit',
                                        borderBottom: item.selected ? '4px solid #FF3F84' : 'inherit',
                                    }}
                                >
                                    <Link
                                        href={item.to}
                                        style={{
                                            width: '100%',
                                            textDecoration: 'none',
                                            color: 'black',
                                        }}
                                    >
                                        <ListItemButton
                                            sx={{
                                                "&:hover": { borderRadius: 2 }
                                            }}
                                        >
                                            <ListItemIcon
                                                sx={{
                                                    color: item.selected ? "black" : theme.palette.quaternary.main,
                                                    minWidth: 32,
                                                }}
                                            >
                                                {MENU_SIDEBAR_ICONS[item.icon]}
                                            </ListItemIcon>

                                            <ListItemText
                                                sx={{
                                                    color: item.selected ? "black" : theme.palette.quaternary.main
                                                }}
                                                primaryTypographyProps={{ fontWeight: 'bold' }}
                                            >
                                                {item.name}
                                                {item.beta && (
                                                    <sup style={{ color: '#FF3F84', paddingLeft: 4, fontWeight: 'bold' }}>Beta</sup>
                                                )}
                                            </ListItemText>
                                        </ListItemButton>
                                    </Link>
                                </ListItem>
                            ))}
                        </Box>
                    ))}
                </List>

                <Box sx={{ p: 2, borderTop: '1px solid #eee' }}>

                    {status === "authenticated" && !!data.user ?
                        <UserMenu user={data.user} />
                        :
                        <Link href="/auth/signin" passHref data-testid="signin-button">
                            <CustomButton sx={{ background: '#E6E7EB', color: theme.palette.quaternary.main, fontWeight: 'bold', padding: 1 }} fullWidth>
                                <LoginOutlined sx={{ marginRight: '8px' }} /> Sign In
                            </CustomButton>
                        </Link>
                    }

                </Box>
            </Box>
        </div>
    )
}

export default MenuSidebarDrawer