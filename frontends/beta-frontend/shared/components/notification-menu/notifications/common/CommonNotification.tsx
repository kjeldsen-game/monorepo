import React from 'react'
import { Box, IconButton, MenuItem, Typography } from '@mui/material'
import { theme } from '@/libs/material/theme'
import { useNotificationApi } from '@/shared/hooks/useNotificationApi'
import CheckIcon from "@mui/icons-material/Check";
import { NotificationResponse } from '@/shared/@types/responses';

export interface CommonNotificationProps {
    children?: React.ReactNode
    id?: string;
}

export interface NotificationProps {
    notification: NotificationResponse;
}

const CommonNotification = ({ children, id }: CommonNotificationProps) => {

    const { handleNotificationRead } = useNotificationApi();

    return (
        <MenuItem sx={{
            cursor: 'default', display: 'flex', justifyContent: 'space-between', alignItems: 'center',
            "&:hover .mark-read-button": {
                visibility: "visible",
            },
        }}>
            <Box sx={{ maxWidth: '80%' }}>
                {children}
                <Typography sx={{ color: theme.palette.quaternary.main }} fontSize={'12px'} variant="subtitle2">
                    25 minutes ago
                </Typography>
            </Box>
            <Box
                className="mark-read-button"
                data-testid="mark-read-button"
                sx={{ visibility: "hidden", maxWidth: '20%' }} >
                <IconButton
                    sx={{ color: theme.palette.secondary.main }}
                    size="small"
                    onClick={() => handleNotificationRead(id)}>
                    <CheckIcon fontSize="small" />
                </IconButton>
            </Box>
        </MenuItem>
    )
}

export default CommonNotification
