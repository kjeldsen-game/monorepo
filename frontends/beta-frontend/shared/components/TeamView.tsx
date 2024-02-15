import { Box, Button, CircularProgress } from '@mui/material'
import TeamDetails from './TeamDetails'
import PlayerTactics from './PlayerTactics'
import TeamTactics from '@/shared/components/TeamTactics'
import { teamColumn } from '@/shared/components/Grid/TeamColumn'
import { SampleTeam } from '@/data/SampleTeam'
import Grid from './Grid/Grid'
import { Player } from '../models/Player'
import { Team } from '../models/Team'

interface TeamProps {
  isEditing: boolean
  team: Team | undefined
  handlePlayerChange?: (value: Player) => void
  onTeamUpdate?: () => void
}

const TeamView: React.FC<TeamProps> = ({ isEditing, team, handlePlayerChange, onTeamUpdate }: TeamProps) => {
  const saveButton = () => {
    if (isEditing) {
      return (
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'right' }}>
          <Button variant="contained" onClick={onTeamUpdate}>
            Save
          </Button>
        </Box>
      )
    }
  }

  return (
    <>
      <Box>
        <Box sx={{ display: 'flex', marginBottom: '2rem', alignItems: 'center' }}>
          <TeamDetails {...SampleTeam} />
          <PlayerTactics />
          <TeamTactics />
        </Box>
        <Box sx={{ minWidth: '80vw' }}>
          {saveButton()}
          {team?.players ? <Grid rows={team?.players} columns={teamColumn(isEditing, handlePlayerChange)} /> : <CircularProgress />}
          {saveButton()}
        </Box>
      </Box>
    </>
  )
}
export default TeamView
