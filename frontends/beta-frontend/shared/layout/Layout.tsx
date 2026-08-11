import { FC, PropsWithChildren } from 'react'
import { Header } from '@/shared/layout/Header'
import { Main } from '@/shared/layout/Main'
import { Item, Sidebar } from '@/shared/layout/Sidebar'
import Box from '@mui/material/Box'
import { useRouter } from 'next/router'
import { SampleSquad } from '@/data/SampleSquad'

export const Layout: FC<PropsWithChildren> = ({ children }) => {
  const { pathname } = useRouter()
  const items: Item[] = [
    { name: 'Dashboard', icon: 'inbox', to: '/dashboard', selected: pathname === '/dashboard' },
    { name: 'Player', icon: 'mail', to: `/player/${SampleSquad[0].id}`, selected: pathname === '/player/[id]' },
    { name: 'Team', icon: 'inbox', to: '/team', selected: pathname === '/team' },
    { name: 'Lineup', icon: 'mail', to: '/lineup', selected: pathname === '/lineup' },
    { name: 'Training', icon: 'mail', to: '/training', hasDivider: false, selected: pathname === '/training' },
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
