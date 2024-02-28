import { useAllTeamsRepository } from '../../api/team/useAllTeamsRepository'
import Grid from '@/shared/components/Grid/Grid'
import { Box } from '@mui/material'
import { leagueColumns } from './LeagueColumns'

const LeagueGrid: React.FC = () => {
  const { allTeams } = useAllTeamsRepository(0, 10)

  return (
    <Box sx={{ width: '100%' }}>
      <Grid rows={allTeams ?? []} columns={leagueColumns} />
    </Box>
  )
}

export { LeagueGrid }
