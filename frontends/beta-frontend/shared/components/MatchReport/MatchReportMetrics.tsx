import { ExpandLess, HealthAndSafety } from '@mui/icons-material'
import { Typography } from '@mui/material'
import Box from '@mui/material/Box'
import Image from 'next/image'

interface MatchReportMetricsProps {
  sx?: React.CSSProperties
  side: 'left' | 'right'
}

export const MatchReportMetrics: React.FC<MatchReportMetricsProps> = ({ sx }) => {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', ...sx }}>
      <Box
        sx={{
          borderStyle: 'solid',
          borderWidth: '0 14px 0 14px',
          borderColor: '#A4BC10',
          width: '100%',
          height: '120px',
          display: 'grid',
          gridTemplateColumns: '120px auto',
          gridTemplateRows: 'auto 25px 40px',
        }}>
        <Box sx={{ gridRow: 'span 3', gridColumn: 1 }}>
          <Image width={120} height={120} alt="team" src="/profile.png" />
        </Box>
        <Typography
          sx={{
            fontSize: '1.5rem',
            gridRow: 1,
            gridColumn: 2,
            padding: '0 1rem 0 1rem',
            display: 'flex',
            justifyContent: 'flex-end',
            alignItems: 'flex-end',
            color: '#A4BC10',
          }}>
          Team name
        </Typography>
        <Typography
          sx={{
            gridRow: 2,
            gridColumn: 2,
            padding: '0 1rem 0 1rem',
            display: 'flex',
            justifyContent: 'flex-end',
            alignItems: 'flex-end',
            color: '#A4BC10',
          }}>
          <ExpandLess />
          League position
        </Typography>
        <Box sx={{ gridRow: '3', gridColumn: '2', display: 'flex', justifyContent: 'space-between', padding: '0 1rem 1rem 1rem' }}>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <HealthAndSafety />
            <Typography fontSize="20px">0</Typography>
          </Box>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <HealthAndSafety />
            <Typography fontSize="20px">0</Typography>
          </Box>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <HealthAndSafety />
            <Typography fontSize="20px">0</Typography>
          </Box>
        </Box>
      </Box>
    </Box>
  )
}

export default MatchReportMetrics
