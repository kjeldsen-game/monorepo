import type { NextPage } from 'next'
import Head from 'next/head'
import { Box, Button, Typography } from '@mui/material'
import Grid from '@/shared/components/Grid/Grid'
import { sampleRows, sampleColumns } from '@/shared/components/Grid/PlayerGrid'
import PlayerDetails from '@/shared/components/PlayerDetails'
import { PlayerStats, samplePlayer } from '@/data/SamplePlayer'
import { useRouter } from 'next/router'
import { PlayerContext, PlayerProvider } from 'contexts/PlayerContext'
import { useContext, useEffect, useState } from 'react'
import { connectorAPI } from '@/libs/fetcher'
import useSWR from 'swr'

interface TeamProps {
  fallback: any;
}

const Player: NextPage<TeamProps> = ({fallback}) => {
  // const playerContext = useContext(PlayerContext)
  let player = {} as PlayerStats
  // console.log('contextplayer', playerContext)


  const router = useRouter();
  const {id} = router.query;
  console.log('id is', id)



  // player = playerContext?.selectedPlayer as PlayerStats
  // const { data, error, isLoading } = useSWR(playerContext?.selectedPlayer ? null : `/player/${id}`, connectorAPI)
  const { data, error, isLoading } = useSWR(`/player/${id}`, connectorAPI)
  if (error) return <div>failed to load</div>
  if (isLoading) return <div>loading...</div>
  if (data) player = data
  console.log('data is', data)

  return (
    <>
      {/* <PlayerProvider> */}
        <Box>
          <PlayerDetails player={player} />
          <Box sx={{ marginBottom: '1rem' }}>
            <Button variant="contained" color="secondary" sx={{ marginRight: '8px', marginBottom: '8px' }}>
              <strong>Main Action</strong>
            </Button>
            <Button variant="outlined" color="secondary" sx={{ marginRight: '8px', marginBottom: '8px' }}>
              <strong>Other Action</strong>
            </Button>
            <Button variant="outlined" color="secondary" sx={{ marginRight: '8px', marginBottom: '8px' }}>
              <strong>Other Action</strong>
            </Button>
            <Button variant="outlined" color="secondary" sx={{ marginRight: '8px', marginBottom: '8px' }}>
              <strong>Other Action</strong>
            </Button>
            <Button variant="outlined" color="secondary" sx={{ marginRight: '8px', marginBottom: '8px' }}>
              <strong>...</strong>
            </Button>
          </Box>

          <Typography sx={{ marginBottom: '1rem', borderBottom: '1px solid' }}>Current Season</Typography>
          <Grid rows={sampleRows} columns={sampleColumns} />
          <Typography sx={{ marginBottom: '1rem', marginTop: '2rem', borderBottom: '1px solid' }}>Previous Season</Typography>
          <Grid rows={sampleRows} columns={sampleColumns} />
          <Typography sx={{ marginBottom: '1rem', marginTop: '2rem', borderBottom: '1px solid' }}>Aggregate</Typography>
          <Grid rows={sampleRows} columns={sampleColumns} />
        </Box>
      {/* </PlayerProvider> */}
    </>
  )
}

export default Player
