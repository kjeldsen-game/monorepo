import { Card, CardProps } from '@mui/material'
import React, { ReactNode } from 'react'

interface ProfileCardProps extends CardProps {
}

const ProfileCard: React.FC<ProfileCardProps> = ({ children, sx }) => {
    return (
        <Card sx={{ padding: '16px', background: 'white', ...sx }}>
            {children}
        </Card>
    )
}

export default ProfileCard