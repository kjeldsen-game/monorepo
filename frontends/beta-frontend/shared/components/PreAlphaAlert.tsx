import { Alert, Typography } from '@mui/material'
import React, { ReactNode } from 'react'

interface PreAlphaAlertProps {
    children?: ReactNode;
}

const PreAlphaAlert: React.FC<PreAlphaAlertProps> = ({ children }) => {
    return (
        <Alert sx={{ mb: '16px', borderLeft: '8px solid #EF7B2B' }} severity="warning">
            <Typography fontWeight={'bold'}>
                Alpha Testing
            </Typography>
            Please note the application is in the Alpha testing so bugs could be find. If you find any bug or would love to have some improvents
            send a message to the Discord community.
            {children}
        </Alert>
    )
}

export default PreAlphaAlert