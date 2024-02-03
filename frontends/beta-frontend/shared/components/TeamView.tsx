import { Box, CircularProgress } from '@mui/material'
import TeamDetails from './TeamDetails'
import PlayerTactics from './PlayerTactics'
import TeamTactics from '@/shared/components/TeamTactics'
import { teamColumn } from '@/shared/components/Grid/TeamColumn'
import { SampleTeam } from '@/data/SampleTeam'
import Grid from './Grid/Grid'
import { Player } from '../models/Player'
import { Team } from '../models/Team'

interface TeamProps {
  team: Team | undefined
  handlePlayerChange?: (value: Player) => void
}

const TeamView: React.FC<TeamProps> = ({ team, handlePlayerChange }: TeamProps) => {
  return (
    <>
      <Box>
        <Box sx={{ display: 'flex', marginBottom: '2rem', alignItems: 'center' }}>
          <TeamDetails {...SampleTeam} />
          <PlayerTactics />
          <TeamTactics />
        </Box>
        <Box sx={{ minWidth: '80vw' }}>
          {team?.players ? <Grid rows={team?.players} columns={teamColumn(true, handlePlayerChange)} /> : <CircularProgress />}
        </Box>
      </Box>
    </>
  )
}
export default TeamView
