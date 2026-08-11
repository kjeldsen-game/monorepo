import type { NextPage } from 'next'
import { Box } from '@mui/material'
import Grid from '@/shared/components/Grid/Grid'
import { SampleSquad, SampleSquadTeamName, SampleSquadStanding } from '@/data/SampleSquad'
import TeamDetails from '@/shared/components/TeamDetails'
import PlayerTactics from '@/shared/components/PlayerTactics'
import TeamTactics from '@/shared/components/TeamTactics'
import { teamColumn } from '@/shared/components/Grid/TeamColumn'

const Team: NextPage = () => {
  const data = SampleSquad

  return (
    <>
      <Box>
        <Box sx={{ display: 'flex', marginBottom: '2rem', alignItems: 'center' }}>
          <TeamDetails name={SampleSquadTeamName} position={SampleSquadStanding} players={[]} />
          <PlayerTactics players={data} />
          <TeamTactics players={data} />
        </Box>
        <Box sx={{ minWidth: '80vw' }}>
          <Grid rows={data} columns={teamColumn} />
        </Box>
      </Box>
    </>
  )
}

export default Team
