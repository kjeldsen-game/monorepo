import { Box } from '@mui/material'
import MatchPlayMessage from './MatchPlayMessage'
import React from 'react'
import { Play } from 'modules/match/types/MatchResponses'
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils'
import PitchAreaTooltip from '../tooltips/PitchAreaTooltip'

interface MatchPlayProps {
    play: Play
    index: number
    isLast: boolean;
}

const MatchPlay: React.FC<MatchPlayProps> = ({ index, isLast, play }) => {
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
                            <PitchAreaTooltip pitchArea={play.duel.pitchArea}>
                                {convertSnakeCaseToTitleCase(play.duel.pitchArea)}
                            </PitchAreaTooltip>{' '}
                            area,
                        </span>
                    )}
                    <MatchPlayMessage
                        duel={play?.duel}
                        action={play.action}
                    />
                    {isLast && (
                        <span style={{ fontSize: '12px', textAlign: 'justify' }}>
                            {` [${play.homeScore}:${play.awayScore}]`}
                        </span>
                    )}{' '}
                </Box>
            </Box>
        </Box>
    )
}

export default MatchPlay