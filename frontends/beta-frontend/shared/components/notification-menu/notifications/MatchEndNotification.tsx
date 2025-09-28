import CommonNotification, { NotificationProps } from './common/CommonNotification'
import { Box, Typography } from '@mui/material'
import Link from 'next/link'
import { theme } from '@/libs/material/theme'

const MatchEndNotification: React.FC<NotificationProps> = ({ notification }) => {
    return (
        <CommonNotification id={notification.id}>
            <Box display="flex" alignItems="center" gap={1}>
                <Typography>{notification.message}</Typography>
                <Link
                    href={`/match/report/${notification.payload.matchId}`}
                    style={{
                        color: theme.palette.secondary.main,
                        textDecoration: 'none',
                        fontWeight: 'bold'
                    }}
                >
                    View
                </Link>
            </Box>
        </CommonNotification>
    )
}

export default MatchEndNotification
