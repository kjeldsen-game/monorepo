import { Box } from '@mui/material'
import { Play } from 'modules/match/types/MatchResponses';
import React from 'react'
import MatchPossesion from './MatchPossession';

interface MatchEventsProps {
    possessions: Play[][];
}

const MatchEvents: React.FC<MatchEventsProps> = ({ possessions }) => {
    return (
        <Box
            display={'flex'}
            flexDirection={'column'}
            alignItems={'center'}
            width={'100%'}
            height={'550px'}
            sx={{ background: 'white', padding: '20px', overflowY: 'scroll' }}>
            {possessions.length === 0 ?
                <Box>
                    No content to be displayed.
                </Box>
                :
                <>
                    <Box
                        sx={{
                            background: '#3D3D3D',
                            width: '200px',
                            height: '20px',
                            color: 'white',
                            borderRadius: '5px',
                            textAlign: 'center',
                            fontSize: '12px',
                        }}>
                        MATCH STARTS
                    </Box>
                    {possessions.map((possesion, index) => (
                        <MatchPossesion
                            key={index}
                            possesion={possesion}
                        />
                    ))}
                </>
            }
        </Box>
    )
}

export default MatchEvents