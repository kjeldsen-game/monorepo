import { Box, Button, CircularProgress, Tooltip, TooltipProps, styled, tooltipClasses } from '@mui/material'
import TeamDetails from './TeamDetails'
import PlayerTactics from './PlayerTactics'
import TeamTactics from '@/shared/components/TeamTactics'
import { teamColumn } from '@/shared/components/Grid/Columns/TeamColumn'
import { SampleTeam } from '@/data/SampleTeam'
import Grid from './Grid/Grid'
import { Player, PlayerStatus } from '../models/Player'
import { Team } from '../models/Team'
import { useEffect, useMemo, useState } from 'react'
import checkTeamComposition from '../utils/TeamCompositionRules'
import TeamCompositionErrors from './TeamCompositionErrors'
import { CompositionError } from '../models/CompositionError'

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
  const [compositionErrors, setCompositionErrors] = useState<CompositionError[]>([])

  const memoizedCheck = useMemo(
    () => checkTeamComposition(team?.players.filter((player) => player.status === PlayerStatus.Active) ?? []),
    [team?.players],
  )

  const memoizedColumns = useMemo(() => teamColumn(isEditing, handlePlayerChange), [isEditing, handlePlayerChange])

  useEffect(() => {
    if (team?.players) {
      setCompositionErrors([...memoizedCheck])
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [team?.players])

  const saveButton = () => {
    if (isEditing) {
      return (
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'right' }}>
          <CompositionTooltip
            disableHoverListener={compositionErrors.length === 0}
            placement={'left'}
            title={<TeamCompositionErrors errors={compositionErrors} />}>
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
    <Box sx={{ width: '100%' }}>
      <Box sx={{ display: 'flex', marginBottom: '2rem', alignItems: 'center' }}>
        <TeamDetails {...SampleTeam} />
        <PlayerTactics />
        <TeamTactics />
      </Box>
      <Box sx={{ width: '100%' }}>
        {saveButton()}
        {team?.players ? <Grid rows={team?.players} columns={memoizedColumns} /> : <CircularProgress />}
        {saveButton()}
      </Box>
    </Box>
  )
}
export default TeamView
