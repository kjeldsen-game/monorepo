import { Play } from '@/shared/models/MatchReport';
import Box from '@mui/material/Box';
import MatchReportItemMessage from './MatchReportItemMessage';

interface MatchReportItemProps {
  event: Play;
  sx?: React.CSSProperties;
  homeId: string;
  awayId: string;
}

export const MatchReportItem: React.FC<MatchReportItemProps> = ({
  sx,
  event,
  homeId,
}) => {
  const formatClock = (clock: number): string => {
    const totalMinutes = Math.floor(clock / 4);
    const seconds = (clock % 4) * 15;

    const formattedMinutes = String(totalMinutes).padStart(2, '0');
    const formattedSeconds = String(seconds).padStart(2, '0');

    return `${formattedMinutes}:${formattedSeconds}`;
  };

  return (
    <Box
      sx={{
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
      }}>
      <Box
        alignSelf={'center'}
        fontSize={'8px'}
        textAlign={'center'}
        sx={{ background: '#A3A3A3', width: '40px' }}
        borderRadius={'5px'}>
        {formatClock(event.clock)}
        {/* {event.clock}:00 */}
      </Box>
      <Box
        paddingY={'10px'}
        display={'flex'}
        justifyContent={
          event.duel.initiator.teamId != homeId ? 'start' : 'end'
        }>
        <Box
          textAlign={event.duel.initiator.teamId != homeId ? 'left' : 'right'}
          sx={{ width: '80%' }}>
          <MatchReportItemMessage
            event={event}
            homeId={homeId}
            duel={event.duel}
            type={event.action}
          />
        </Box>
      </Box>
    </Box>
  );
};

export default MatchReportItem;
