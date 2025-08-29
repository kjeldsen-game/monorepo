import { Box } from '@mui/material';
import React from 'react';
import { Play } from 'modules/match/types/MatchResponses';
import MatchPlay from './MatchPlay';
import { formatClock } from 'modules/match/utils/MatchReportUtils';

interface MatchPossesionProps {
    possesion: Play[];
}

const MatchPossesion: React.FC<MatchPossesionProps> = ({
    possesion,
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
                    {possesion.map((play: Play, index: number) => {
                        const isLast = index === possesion.length - 1;
                        return (
                            <MatchPlay
                                index={index}
                                key={index}
                                play={play}
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
