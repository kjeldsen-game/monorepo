import { MatchEvent } from '@/shared/models/MatchEvent'
import Box from '@mui/material/Box'

interface MatchReportItemProps {
  value: string
  eventType: MatchEvent
  sx?: React.CSSProperties
}

const styles: Record<MatchEvent, React.CSSProperties> = {
  MainEvent: {
    backgroundColor: 'black',
    color: 'white',
    textAlign: 'center',
  },
  HomeTeamEvent: {
    color: 'green',
    textAlign: 'left',
  },
  AwayTeamEvent: {
    color: 'red',
    textAlign: 'right',
  },
}

export const MatchReportItem: React.FC<MatchReportItemProps> = ({ sx, value, eventType }) => {
  return (
    <Box
      sx={{
        alignItems: 'center',
        height: '34px',
        width: '100%',
        ...styles[eventType],
        ...sx,
      }}>
      {value}
    </Box>
  )
}

export default MatchReportItem
