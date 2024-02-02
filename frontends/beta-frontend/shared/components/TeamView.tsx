import { Box, CircularProgress } from '@mui/material'
import TeamDetails from './TeamDetails'
import PlayerTactics from './PlayerTactics'
import TeamTactics from '@/shared/components/TeamTactics'
import { teamColumn } from '@/shared/components/Grid/TeamColumn'
import { SampleTeam } from '@/data/SampleTeam'
import Grid from './Grid/Grid'

interface TeamProps {
  team: { players: unknown[] }
}

// eslint-disable-next-line react/prop-types
const TeamView: React.FC<TeamProps> = ({ team }) => {
  return (
    <>
      <Box>
        <Box sx={{ display: 'flex', marginBottom: '2rem', alignItems: 'center' }}>
          <TeamDetails {...SampleTeam} />
          <PlayerTactics />
          <TeamTactics />
        </Box>
        <Box sx={{ minWidth: '80vw' }}>{team?.players ? <Grid rows={team?.players} columns={teamColumn(true)} /> : <CircularProgress />}</Box>
      </Box>
    </>
  )
}
export default TeamView
