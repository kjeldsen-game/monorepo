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
        {event.clock}:00
      </Box>
      <Box
        paddingY={'10px'}
        display={'flex'}
        justifyContent={
          event.duel.initiator.teamId != homeId ? 'start' : 'end'
        }>
        <Box
          textAlign={event.duel.initiator.teamId != homeId ? 'left' : 'right'}
          sx={{ width: '50%' }}>
          <MatchReportItemMessage
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
