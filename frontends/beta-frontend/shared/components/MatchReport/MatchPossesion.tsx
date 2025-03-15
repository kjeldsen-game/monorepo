import { Box } from '@mui/material';
import React from 'react';
import MatchReportItem from './MatchReportItem';

interface MatchPossesionProps {
  possesion: any;
  homeId: string;
  awayId: string;
}

const MatchPossesion: React.FC<MatchPossesionProps> = ({
  possesion,
  homeId,
  awayId,
}) => {
  console.log(possesion);
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
        {formatClock(possesion[0].clock)}
        {/* {event.clock}:00 */}
      </Box>
      <Box
        paddingY={'10px'}
        display={'flex'}
        // sx={{ background: 'red' }}
        flexDirection={'column'}
        alignItems={
          possesion[0].duel.initiator.teamRole === 'HOME' ? 'start' : 'end'
        }>
        <Box
          // textAlign={event.duel.initiator.teamId != homeId ? 'left' : 'right'}
          textAlign={'justify'}
          sx={{ width: '80%' }}>
          {possesion.map((event: any, index: number) => {
            const isLast = index === possesion.length - 1;
            console.log(isLast);
            console.log(index);
            return (
              <MatchReportItem
                index={index}
                key={index}
                event={event}
                homeId={homeId}
                awayId={awayId}
                isLast={isLast}
              />
            );
          })}
        </Box>
      </Box>
    </Box>
  );
};

export default MatchPossesion;
