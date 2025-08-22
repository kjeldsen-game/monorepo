import { theme } from '@/libs/material/theme'
import { Player } from '@/shared/models/player/Player'
import { getSurname } from '@/shared/utils/PlayerUtils'
import { useMediaQuery } from '@mui/material'
import React from 'react'

interface LineupButtonTextProps {
    player?: Player
}

const LineupButtonText: React.FC<LineupButtonTextProps> = ({ player }) => {

    const isXs = useMediaQuery(theme.breakpoints.down("sm"))

    return (
        <div
            style={{
                ...!isXs ?
                    { paddingLeft: '4px' } :
                    { paddingBottom: '0px' },
                color: 'black',
                overflow: 'hidden',
                whiteSpace: 'nowrap',
                textTransform: 'capitalize',
            }}>
            {getSurname(player?.name)}
        </div>
    )
}

export default LineupButtonText