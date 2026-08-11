import type { NextPage } from 'next'
import { Box, Button, Typography } from '@mui/material'
import Grid from '@/shared/components/Grid/Grid'
import { sampleRows, sampleColumns } from '@/shared/components/Grid/PlayerGrid'
import PlayerDetails from '@/shared/components/PlayerDetails'
import { SampleSquad } from '@/data/SampleSquad'
import { useRouter } from 'next/router'

const Player: NextPage = () => {
  const router = useRouter()
  const { id } = router.query

  const squadPlayer = SampleSquad.find((p) => String(p.id) === id) || SampleSquad[0]
  const player = {
    name: squadPlayer.name,
    position: squadPlayer.position,
    age: squadPlayer.age,
    actualSkills: {
      DEFENSE_POSITION: squadPlayer.actualSkills.DEFENSE_POSITION.PlayerSkills.actual,
      BALL_CONTROL: squadPlayer.actualSkills.BALL_CONTROL.PlayerSkills.actual,
      SCORE: squadPlayer.actualSkills.SCORE.PlayerSkills.actual,
      PASSING: squadPlayer.actualSkills.PASSING.PlayerSkills.actual,
      OFFENSIVE_POSITION: squadPlayer.actualSkills.OFFENSIVE_POSITION.PlayerSkills.actual,
      TACKLING: squadPlayer.actualSkills.TACKLING.PlayerSkills.actual,
      CO: squadPlayer.actualSkills.CO.PlayerSkills.actual,
    },
  }

  return (
    <>
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
    </>
  )
}

export default Player
