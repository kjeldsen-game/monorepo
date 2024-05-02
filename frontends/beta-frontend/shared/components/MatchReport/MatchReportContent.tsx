import Box from '@mui/material/Box'
import MatchReportItem from './MatchReportItem'

interface MatchReportContentProps {
  sx?: React.CSSProperties
}

export const MatchReportContent: React.FC<MatchReportContentProps> = ({ sx }) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        height: '100%',
        overflow: 'auto',
        ...sx,
      }}>
      <MatchReportItem value={'MatchStarts'} eventType="MainEvent" />
      <MatchReportItem value={'Goal'} eventType="HomeTeamEvent" />
      <MatchReportItem value={'Goal'} eventType="HomeTeamEvent" />
      <MatchReportItem value={'Goal'} eventType="AwayTeamEvent" />
      <MatchReportItem value={'Goal'} eventType="HomeTeamEvent" />
      <MatchReportItem value={'MatchEnds'} eventType="MainEvent" />
    </Box>
  )
}

export default MatchReportContent
