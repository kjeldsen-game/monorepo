import Box from '@mui/material/Box'
import MatchReportItem from './MatchReportItem'
import { MatchReportType } from '@/shared/models/MatchReport'
import { MatchEvent } from '@/shared/models/MatchEvent'

interface MatchReportContentProps {
  report: MatchReportType
  sx?: React.CSSProperties
}

export const MatchReportContent: React.FC<MatchReportContentProps> = ({ report, sx }) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        height: '100%',
        overflow: 'auto',
        gap: '15px',
        padding: '0 5px 0 5px',
        ...sx,
      }}>
      {report.events.map((event, index) => (
        <MatchReportItem key={index} event={event} />
      ))}
    </Box>
  )
}

export default MatchReportContent
