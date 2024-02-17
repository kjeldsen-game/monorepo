import { Box, Button, CircularProgress, Tooltip, TooltipProps, styled, tooltipClasses } from '@mui/material'
import TeamDetails from './TeamDetails'
import PlayerTactics from './PlayerTactics'
import TeamTactics from '@/shared/components/TeamTactics'
import { teamColumn } from '@/shared/components/Grid/TeamColumn'
import { SampleTeam } from '@/data/SampleTeam'
import Grid from './Grid/Grid'
import { Player } from '../models/Player'
import { Team } from '../models/Team'
import { useEffect, useState } from 'react'
import checkTeamComposition from '../utils/TeamCompositionRules'
import TeamCompositionErrors from './TeamCompositionErrors'

const CompositionTooltip = styled(({ className, ...props }: TooltipProps) => <Tooltip {...props} classes={{ popper: className }} />)(() => ({
  [`& .${tooltipClasses.tooltip}`]: {
    maxWidth: 500,
  },
}))

interface TeamProps {
  isEditing: boolean
  team: Team | undefined
  handlePlayerChange?: (value: Player) => void
  onTeamUpdate?: () => void
}

const TeamView: React.FC<TeamProps> = ({ isEditing, team, handlePlayerChange, onTeamUpdate }: TeamProps) => {
  const [compositionErrors, setCompositionErrors] = useState<string[]>([])

  useEffect(() => {
    if (team?.players) {
      setCompositionErrors([...checkTeamComposition(team.players)])
    }
  }, [team?.players])

  const saveButton = () => {
    if (isEditing) {
      return (
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'right' }}>
          <CompositionTooltip placement={'left'} title={<TeamCompositionErrors errors={compositionErrors} />}>
            <span>
              <Button variant="contained" onClick={onTeamUpdate} disabled={compositionErrors.length > 0}>
                Save
              </Button>
            </span>
          </CompositionTooltip>
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
