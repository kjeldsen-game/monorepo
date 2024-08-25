import { useTeamRepository } from '@/pages/api/team/useTeamRepository'
import { HealthAndSafety } from '@mui/icons-material'
import { Typography } from '@mui/material'
import Box from '@mui/material/Box'
import Image from 'next/image'
import Grid from '../Grid/Grid'
import { useMemo } from 'react'
import { simpleTeamColumn } from '../Grid/Columns/SimpleTeamColumn'
import { useTranslation } from 'react-i18next'

interface MatchReportMetricsProps {
  sx?: React.CSSProperties
  teamId: string
  side: 'left' | 'right'
}

export const MatchReportMetrics: React.FC<MatchReportMetricsProps> = ({ sx, teamId }) => {
  const { data } = useTeamRepository(teamId)

  const { t } = useTranslation(['game'])

  const memoizedColumns = useMemo(() => simpleTeamColumn(t), [teamId])

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', ...sx }}>
      <Box
        sx={{
          borderStyle: 'solid',
          borderWidth: '0 14px 0 14px',
          borderColor: '#A4BC10',
          width: '100%',
          height: '120px',
          display: 'flex',
          flexFlow: 'column wrap',
          justifyContent: 'space-around',
        }}>
        <Typography
          sx={{
            fontSize: '22px',
            color: '#A4BC10',
            overflow: 'clip',
            height: '20px',
            lineHeight: '20px',
            width: '100px',
          }}>
          {data?.name}
        </Typography>
        <Image width={100} height={100} alt="team logo" src="/profile.png" />
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <HealthAndSafety />
          <Typography fontSize="20px">Swarm centre</Typography>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <HealthAndSafety />
          <Typography fontSize="20px">Vertical pressure</Typography>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <HealthAndSafety />
          <Typography fontSize="20px">Tiki Taka</Typography>
        </Box>
      </Box>
      <Box>
        <Grid rows={data?.players ?? []} columns={memoizedColumns} />
      </Box>
    </Box>
  )
}

export default MatchReportMetrics
