import { theme } from '@/libs/material/theme'
import { Theme } from '@emotion/react'
import { Box, SxProps, Typography } from '@mui/material'
import React from 'react'

interface NoDataErrorProps {
    icon?: React.ElementType
    title?: string
    subtitle?: string
    sx?: SxProps<Theme>
}

const NoDataError: React.FC<NoDataErrorProps> = ({ icon: IconComponent, title, subtitle }) => {
    return (
        <Box
            padding={2}
            display="flex"
            flexDirection="column"
            justifyContent="center"
            alignItems="center"
            textAlign="center"
            height={'100%'}
        >
            {IconComponent && <IconComponent sx={{ fontSize: 40, color: theme.palette.secondary.main, mb: 1 }} />}
            <Typography fontSize={20} fontWeight="bold">
                {title}
            </Typography>
            <Typography variant="subtitle2" sx={{ color: theme.palette.quaternary.main, mt: 1 }}>
                {subtitle}
            </Typography>
        </Box>
    )
}

export default NoDataError
