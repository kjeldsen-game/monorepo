import { Play } from '@/shared/models/MatchReport';
import Box from '@mui/material/Box';
import MatchReportItemMessage from './MatchReportItemMessage';
import PitchAreaTooltip from './Tooltips/PitchAreaTooltip';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';

interface MatchReportItemProps {
  event: Play;
  sx?: React.CSSProperties;
  homeId: string;
  awayId: string;
  index: number;
  isLast: boolean;
}

export const MatchReportItem: React.FC<MatchReportItemProps> = ({
  sx,
  event,
  homeId,
  index,
  isLast,
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
        borderRadius={'5px'}></Box>
      <Box display={'flex'}>
        <Box textAlign={'justify'}>
          {index === 0 && (
            <span style={{ fontSize: '12px', textAlign: 'justify' }}>
              In the{' '}
              <PitchAreaTooltip pitchArea={event.duel.pitchArea}>
                {convertSnakeCaseToTitleCase(event.duel.pitchArea)}
              </PitchAreaTooltip>{' '}
              area,
            </span>
          )}
          <MatchReportItemMessage
            event={event}
            homeId={homeId}
            duel={event.duel}
            type={event.action}
          />
          {isLast && (
            <span style={{ fontSize: '12px', textAlign: 'justify' }}>
              {` [${event.homeScore}:${event.awayScore}]`}
            </span>
          )}{' '}
        </Box>
      </Box>
    </Box>
  );
};

export default MatchReportItem;
