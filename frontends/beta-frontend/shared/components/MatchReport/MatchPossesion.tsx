import { Box } from '@mui/material';
import React from 'react';
import MatchReportItem from './MatchReportItem';
import { formatClock } from '@/shared/utils/MatchReportUtils';

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
      </Box>
      <Box
        paddingY={'10px'}
        display={'flex'}
        flexDirection={'column'}
        alignItems={
          possesion[0].duel.initiator.teamRole === 'HOME' ? 'start' : 'end'
        }>
        <Box textAlign={'justify'} sx={{ width: '80%' }}>
          {possesion.map((event: any, index: number) => {
            const isLast = index === possesion.length - 1;
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
