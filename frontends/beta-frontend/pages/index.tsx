import type { NextPage } from 'next'
import Head from 'next/head'
import { Typography } from '@mui/material'

const Home: NextPage = () => {
  return (
    <>
      <Head>
        <title>Welcome to Kjedlsen</title>
        <meta name="description" content="Generated by create next app" />
      </Head>
      <>
        <Typography variant="h1" component="h1">
          Welcome to Kjedlsen
        </Typography>
      </>
    </>
  )
}

export default Home