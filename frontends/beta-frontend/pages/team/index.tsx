import type { NextPage } from 'next'
import Head from 'next/head'
import { Box } from '@mui/material'
import Grid from '@/shared/components/Grid/Grid'
import { SampleTeam } from '@/data/SampleTeam'
import TeamDetails from '@/shared/components/TeamDetails'
import PlayerTactics from '@/shared/components/PlayerTactics'
import TeamTactics from '@/shared/components/TeamTactics'
import { samplePlayerColumn } from '@/data/samplePlayerColumn'
import useSWR from "swr"
import { connectorAPI } from "@/libs/fetcher"
import { PlayerProvider } from 'contexts/PlayerContext'

const Team: NextPage = () => {
  const { data, error, isLoading } = useSWR('/player?size=40&page=0', connectorAPI)
  if (error) return <div>failed to load</div>
  if (isLoading) return <div>loading...</div>

  // console.log(typeof data)
  // console.log(data)
  // data.forEach((element: any) => console.log(element))

  return (
    <>
      <Head>
        <title>Kjeldsen</title>
        <meta name="description" content="Generated by create next app" />
      </Head>
      <Box>
        <Box sx={{ display: 'flex', marginBottom: '2rem', alignItems: 'center' }}>
          <TeamDetails {...SampleTeam} />
          <PlayerTactics />
          <TeamTactics />
        </Box>
        <Box sx={{ minWidth: '80vw' }}>
          <Grid rows={data} columns={samplePlayerColumn} />
        </Box>
      </Box>
    </>
  )
}

export default Team
