import { FC, PropsWithChildren } from 'react'
import { Header } from '@/shared/layout/Header'
import { Main } from '@/shared/layout/Main'

export const Layout: FC<PropsWithChildren> = ({ children }) => {
  return (
    <>
      <Header />
      <Main>{children}</Main>
    </>
  )
}
