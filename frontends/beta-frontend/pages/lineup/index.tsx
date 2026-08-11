import type { NextPage } from 'next'
import Head from 'next/head'
import { Box, Typography } from '@mui/material'
import { SampleSquad, SampleSquadTeamName, SampleSquadStanding } from '@/data/SampleSquad'
import TeamDetails from '@/shared/components/TeamDetails'
import PlayerTactics from '@/shared/components/PlayerTactics'
import TeamTactics from '@/shared/components/TeamTactics'
import Pitch from '@/shared/components/Pitch'

const byPosition = (position: string, count: number) => SampleSquad.filter((p) => p.position === position).slice(0, count)

const startingXI = [...byPosition('GOALKEEPER', 1), ...byPosition('DEFENDER', 4), ...byPosition('MIDDLE', 4), ...byPosition('FORWARD', 2)]

const Lineup: NextPage = () => {
  return (
    <>
      <Head>
        <title>Kjeldsen - Lineup</title>
      </Head>
      <Box>
        <Typography variant="h4" component="h1" sx={{ marginBottom: '2rem' }}>
          Lineup
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: '2rem' }}>
          <TeamDetails name={SampleSquadTeamName} position={SampleSquadStanding} players={[]} />
        </Box>
        <Box sx={{ display: 'flex', gap: '3rem', flexWrap: 'wrap', alignItems: 'flex-start' }}>
          <Pitch players={startingXI} />
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
            <Typography variant="h6">Set Piece Takers</Typography>
            <Box sx={{ display: 'flex', gap: '2rem' }}>
              <PlayerTactics players={SampleSquad} />
              <TeamTactics players={SampleSquad} />
            </Box>
          </Box>
        </Box>
      </Box>
    </>
  )
}

export default Lineup
