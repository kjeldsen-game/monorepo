import { Box, Typography } from '@mui/material'
import SocialMediaButtons from './SocialMediaButtons'

const AuthFooter = () => {
    return (
        <Box mt={'20px'} textAlign={'center'} >
            <Box sx={{ opacity: '0.5' }}>
                <Typography>
                    Kjeldsen 2023 · All rights reserved
                </Typography>
                <Typography>hello@kjeldsen.com</Typography>
            </Box>
            <SocialMediaButtons />
        </Box>
    )
}

export default AuthFooter