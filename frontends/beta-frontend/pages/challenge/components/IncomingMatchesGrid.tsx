import Grid from '@/shared/components/Grid/Grid'
import { Box } from '@mui/material'
import { useState } from 'react'
import { useSession } from 'next-auth/react'
import { useAllPlayerMatchesRepository } from '@/pages/api/match/useAllPlayerMatchesRepository'
import { useTranslation } from 'next-i18next'
import incomingMatchesColumns from '@/shared/components/Grid/Columns/IncomingMatchesColumns'

const PAGE_SIZE = 10

interface IncomingMatchesGridProps {}

const IncomingMatchesGrid: React.FC<IncomingMatchesGridProps> = () => {
  const { data: userData } = useSession({ required: true })

  const { t } = useTranslation('common')

  const [selectedPage, setSelectedPage] = useState<number>(0)

  const { allMatches } = useAllPlayerMatchesRepository(selectedPage, 10, userData?.user.teamId)

  if (!allMatches || allMatches.length === 0) {
    return <Box sx={{ width: '100%' }}>{t('challenge.no_incoming_matches')}</Box>
  }

  return (
    <Box sx={{ width: '100%' }}>
      <Grid
        isRowSelectable={() => false}
        rows={allMatches ?? []}
        columns={userData?.user.teamId ? incomingMatchesColumns() : []}
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

export { IncomingMatchesGrid }
export default IncomingMatchesGrid
