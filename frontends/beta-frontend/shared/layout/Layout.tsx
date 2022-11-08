import { FC, PropsWithChildren } from 'react'
import { Header } from '@/shared/layout/Header'
import { Main } from '@/shared/layout/Main'
import { Item, Sidebar } from '@/shared/layout/Sidebar'
import Box from '@mui/material/Box'
import { useRouter } from 'next/router'

export const Layout: FC<PropsWithChildren> = ({ children }) => {
  const { pathname } = useRouter()
  const items: Item[] = [
    { name: 'Dashboard', icon: 'inbox', to: '/dashboard', selected: pathname === '/dashboard' },
    { name: 'TempPlayerView', icon: 'mail', to: '/player', selected: pathname === '/player' },
    { name: 'Team', icon: 'inbox', to: '/team', selected: pathname === '/team' },
    // { name: 'Training', icon: 'mail', to: '/training', hasDivider: false, selected: pathname === '/training' },
    // { name: 'Generate Player', icon: 'inbox', to: '/', selected: pathname === '/' },
    // { name: 'Generate Match', icon: 'mail', to: '/', selected: pathname === '/' },
  ]
  return (
    <>
      <Header />
      <Box sx={{ display: 'flex' }}>
        <Sidebar items={items} />
        <Main>{children}</Main>
      </Box>
    </>
  )
}
