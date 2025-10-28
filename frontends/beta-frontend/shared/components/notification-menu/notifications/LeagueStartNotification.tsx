import CommonNotification, { NotificationProps } from './common/CommonNotification'
import { Box, Typography } from '@mui/material'
import Link from 'next/link'
import { theme } from '@/libs/material/theme'

const LeagueStartNotification: React.FC<NotificationProps> = ({ notification }) => {
    return (
        <CommonNotification id={notification.id}>
            <Box display="flex" alignItems="center" gap={1}>
                <Typography sx={{ whiteSpace: 'normal' }}>{notification.message}
                    <Link
                        href={`/league`}
                        style={{
                            paddingLeft: '8px',
                            color: theme.palette.secondary.main,
                            textDecoration: 'none',
                            fontWeight: 'bold'
                        }}>
                        View
                    </Link>
                </Typography>
            </Box>
        </CommonNotification>
    )
}

export default LeagueStartNotification
