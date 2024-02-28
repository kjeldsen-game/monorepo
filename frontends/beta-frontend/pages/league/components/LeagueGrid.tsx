import { useAllTeamsRepository } from '../../api/team/useAllTeamsRepository'
import Grid from '@/shared/components/Grid/Grid'
import { Box } from '@mui/material'
import { leagueColumns } from './LeagueColumns'
import { useState } from 'react'

const PAGE_SIZE = 10

const LeagueGrid: React.FC = () => {
  const [selectedPage, setSelectedPage] = useState<number>(0)
  const { allTeams } = useAllTeamsRepository(selectedPage, PAGE_SIZE)

  return (
    <Box sx={{ width: '100%' }}>
      <Grid
        rows={allTeams ?? []}
        columns={leagueColumns}
        paginationMode="server"
        pagination
        pageSize={PAGE_SIZE}
        hideFooter={false}
        onPageChange={(value) => setSelectedPage(value)}
        // TODO: Get total of teams from the API
        rowCount={10}
      />
    </Box>
  )
}

export { LeagueGrid }
