import React from 'react'
import CommonNotification, { NotificationProps } from './common/CommonNotification'
import { Box, Typography } from '@mui/material'
import Link from 'next/link'
import { theme } from '@/libs/material/theme'

const PlayerBidNotification: React.FC<NotificationProps> = ({ notification }) => {
    return (
        <CommonNotification id={notification.id}>
            <Box
                display="flex"
                alignItems="center"
                flexWrap="wrap"
            >
                <Typography
                    sx={{
                        flex: '1 1 100%',
                        wordBreak: 'break-word',
                        whiteSpace: 'normal',
                    }}
                >
                    {notification.message}
                </Typography>
                <Link
                    href={`/player/${notification.payload.playerId}`}
                    style={{
                        color: theme.palette.secondary.main,
                        textDecoration: 'none',
                        fontWeight: 'bold',
                    }}
                >
                    View
                </Link>
            </Box>
        </CommonNotification>

    )
}

export default PlayerBidNotification