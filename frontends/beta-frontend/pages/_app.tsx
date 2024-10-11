import React, { ReactElement, ReactNode } from 'react'
import { NextPage } from 'next'
import Head from 'next/head'
import { AppProps } from 'next/app'
import { ThemeProvider } from '@mui/material/styles'
import CssBaseline from '@mui/material/CssBaseline'
import { CacheProvider, EmotionCache } from '@emotion/react'
import { SessionProvider } from 'next-auth/react'
import { Session } from 'next-auth'
import { createEmotionCache } from '@/libs/emotion/cache'
import { theme } from '@/libs/material/theme'
import { Layout } from '@/shared/layout'
import { GameUser } from '@/shared/models/GameUser'
import { appWithTranslation } from 'next-i18next'
import { LocalizationProvider } from '@mui/x-date-pickers'
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment'

export type NextPageWithLayout<P = object, IP = P> = NextPage<P, IP> & {
  getLayout?: (page: ReactElement) => ReactNode
}

// Client-side cache, shared for the whole session of the user in the browser.
const clientSideEmotionCache = createEmotionCache()

interface MyAppProps<P> extends AppProps<P> {
  Component: NextPageWithLayout
  emotionCache?: EmotionCache
}

interface GameSession extends Session {
  user: GameUser
}

interface MyExtendedPageProps {
  session: GameSession
}

const defaultGetLayout = (page: ReactElement) => <Layout>{page}</Layout>

function MyApp({ Component, emotionCache = clientSideEmotionCache, pageProps: { session, ...pageProps } }: MyAppProps<MyExtendedPageProps>) {
  const getLayout = Component.getLayout ?? defaultGetLayout

  return (
    <SessionProvider session={session}>
      <LocalizationProvider dateAdapter={AdapterMoment}>
        <CacheProvider value={emotionCache}>
          <Head>
            <meta name="viewport" content="initial-scale=1, width=device-width" />
            <title>Kjeldsen</title>
          </Head>
          <ThemeProvider theme={theme}>
            {/* CssBaseline kickstart an elegant, consistent, and simple baseline to build upon. */}
            <CssBaseline />
            {getLayout(<Component {...pageProps} />)}
          </ThemeProvider>
        </CacheProvider>
      </LocalizationProvider>
    </SessionProvider>
  )
}

export default appWithTranslation(MyApp)
