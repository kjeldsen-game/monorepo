import { FC, PropsWithChildren } from 'react';
import { Header } from '@/shared/layout/Header';
import { Main } from '@/shared/layout/Main';
import { Item, Sidebar } from '@/shared/layout/Sidebar';
import Box from '@mui/material/Box';
import { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';

export const Layout: FC<PropsWithChildren> = ({ children }) => {
  const { pathname } = useRouter();

  const { t } = useTranslation('common');

  const items: Item[] = [
    // {
    //   name: t('Dashboard'),
    //   icon: 'inbox',
    //   to: '/dashboard',
    //   selected: pathname === '/dashboard',
    // },
    {
      name: 'Team',
      icon: 'team',
      to: '/team',
      selected: pathname === '/team',
    },
    // {
    //   name: 'Economy',
    //   icon: 'market',
    //   to: '/team/economy',
    //   hasDivider: false,
    //   selected: pathname === '/team/economy',
    // },
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
    // { name: 'Generate Player', icon: 'inbox', to: '/', selected: pathname === '/' },
    // { name: 'Generate Match', icon: 'mail', to: '/', selected: pathname === '/' },
  ];
  return (
    <>
      <Header />

      <Box sx={{ display: 'flex' }}>
        <Sidebar items={items} />
        <Main>{children}</Main>
      </Box>
    </>
  );
};
