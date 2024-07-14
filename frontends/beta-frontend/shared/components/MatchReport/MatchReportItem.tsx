import { MatchActionType, MatchEvent, MatchEventSide } from '@/shared/models/MatchEvent'
import { SportsSoccer, ArrowForward, ControlCamera, KeyboardDoubleArrowDown } from '@mui/icons-material'
import Box from '@mui/material/Box'
interface MatchReportItemProps {
  event: MatchEvent
  sx?: React.CSSProperties
}

const justifyStyles: Record<MatchEventSide, React.CSSProperties> = {
  MainEvent: {
    justifyContent: 'center',
  },
  HomeTeamEvent: {
    justifyContent: 'left',
  },
  AwayTeamEvent: {
    justifyContent: 'right',
  },
}

const colorStyles: Record<MatchEventSide, React.CSSProperties> = {
  MainEvent: {
    color: 'white',
  },
  HomeTeamEvent: {
    color: 'green',
  },
  AwayTeamEvent: {
    color: 'red',
  },
}

const iconByAction: Record<MatchActionType, React.ReactNode> = {
  PASS: <ArrowForward />,
  POSITION: <ControlCamera />,
  TACKLE: <KeyboardDoubleArrowDown />,
  SHOOT: <SportsSoccer />,
}

export const MatchReportItem: React.FC<MatchReportItemProps> = ({ sx, event }) => {
  const startCharPos = event.eventStart.indexOf(event.actionStats.player1.playerName)
  const endCharPos = startCharPos + event.actionStats.player1.playerName.length
  return (
    <Box
      sx={{
        alignItems: 'center',
        width: '100%',
        display: 'flex',
        flexDirection: 'row',
        ...justifyStyles[event.eventSide],
        ...sx,
      }}>
      {iconByAction[event.action]}
      <span
        style={{
          color: 'black',
          fontWeight: 'bold',
          marginRight: '5px',
          ...colorStyles[event.eventSide],
        }}>
        {event.eventStart.slice(startCharPos, endCharPos)}
      </span>
      <span>{event.eventStart.slice(endCharPos)}</span>
    </Box>
  )
}

export default MatchReportItem
