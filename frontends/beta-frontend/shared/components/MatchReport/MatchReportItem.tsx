import { MatchActionType, MatchEventSide, Play } from '@/shared/models/MatchReport'
import { SportsSoccer, ArrowForward, ControlCamera, KeyboardDoubleArrowDown } from '@mui/icons-material'
import Box from '@mui/material/Box'
interface MatchReportItemProps {
  event: Play
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
  PASSING: <ArrowForward />,
  POSITIONAL: <ControlCamera />,
  TACKLE: <KeyboardDoubleArrowDown />,
  SHOT: <SportsSoccer />,
}

export const MatchReportItem: React.FC<MatchReportItemProps> = ({ sx, event }) => {
  return (
    <Box
      sx={{
        alignItems: 'center',
        width: '100%',
        display: 'flex',
        flexDirection: 'row',
        ...justifyStyles[event.duel.side],
        ...sx,
      }}>
      {iconByAction[event.duel.type]}
      <span
        style={{
          color: 'black',
          fontWeight: 'bold',
          marginRight: '5px',
          ...colorStyles[event.duel.side],
        }}>
        {event.duel.initiator.name}
        {event.duel.type}
      </span>
      <span>{event.duel.challenger?.name}</span>
      <span>{event.duel.receiver?.name}</span>
    </Box>
  )
}

export default MatchReportItem
