import { Card, CardContent } from '@mui/material'
import React, { ReactNode } from 'react'

interface AuthFormCardProps {
    children: ReactNode
}

const AuthFormCard: React.FC<AuthFormCardProps> = ({ children }) => {
    return (
        <Card
            sx={{
                mt: '24px',
                width: '320px',
                border: '2px solid #FF3F84',
                borderRadius: '20px',
            }}>
            <CardContent sx={{ padding: '24px !important' }}>
                {children}
            </CardContent>
        </Card>
    )
}

export default AuthFormCard