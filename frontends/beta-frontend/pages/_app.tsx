import { ReactElement, ReactNode } from 'react';
import { NextPage } from 'next';
import Head from 'next/head';
import { AppProps } from 'next/app';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { CacheProvider, EmotionCache } from '@emotion/react';
import { SessionProvider } from 'next-auth/react';
import { Session } from 'next-auth';
import { createEmotionCache } from '@/libs/emotion/cache';
import { theme } from '@/libs/material/theme';
import { GameUser } from '@/shared/models/GameUser';
import { appWithTranslation } from 'next-i18next';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import { Layout } from '@/shared/layout/Layout';
import { ErrorProvider } from '@/shared/contexts/ErrorContext';
import ErrorSnackbar from '@/shared/components/Common/ErrorSnackbar';
import { NotificationProvider } from '@/shared/contexts/NotificationContext';
import NotificationSnackbar from '@/shared/components/Common/NotificationSnackbar';
import { SnackbarProvider } from 'notistack';

export type NextPageWithLayout<P = object, IP = P> = NextPage<P, IP> & {
  getLayout?: (page: ReactElement) => ReactNode;
};

// Client-side cache, shared for the whole session of the user in the browser.
const clientSideEmotionCache = createEmotionCache();

interface MyAppProps<P> extends AppProps<P> {
  Component: NextPageWithLayout;
  emotionCache?: EmotionCache;
}

interface GameSession extends Session {
  user: GameUser;
}

interface MyExtendedPageProps {
  session: GameSession;
}

const defaultGetLayout = (page: ReactElement) => <Layout>{page}</Layout>;

function MyApp({
  Component,
  emotionCache = clientSideEmotionCache,
  pageProps: { session, ...pageProps },
}: MyAppProps<MyExtendedPageProps>) {
  const getLayout = Component.getLayout ?? defaultGetLayout;

  return (
    <SessionProvider session={session}>
      <LocalizationProvider dateAdapter={AdapterMoment}>
        <CacheProvider value={emotionCache}>
          <Head>
            <meta
              name="viewport"
              content="initial-scale=1, width=device-width"
            />
            <title>Kjeldsen</title>
          </Head>
          <ThemeProvider theme={theme}>
            <SnackbarProvider
              maxSnack={4}
              autoHideDuration={1500}
              anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
            >
              <ErrorProvider>
                <NotificationProvider>
                  <CssBaseline />
                  <ErrorSnackbar />
                  <NotificationSnackbar />
                  {getLayout(<Component {...pageProps} />)}
                </NotificationProvider>
              </ErrorProvider>
            </SnackbarProvider>
          </ThemeProvider>
        </CacheProvider>
      </LocalizationProvider>
    </SessionProvider>
  );
}

export default appWithTranslation(MyApp);
