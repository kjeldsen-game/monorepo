import { Box, Grid } from '@mui/material'
import React from 'react'
import CustomTextField from '../Common/CustomTextField'
import CustomButton from '../Common/CustomButton'

interface ChangeProfileInformationSectionProps {
    teamName?: string,
    email?: string;
}

const ChangeProfileInformationSection: React.FC<ChangeProfileInformationSectionProps> = ({ teamName, email }) => {
    return (
        <>
            <Grid spacing={2} container sx={{ width: '100%', my: '16px' }}>
                <Grid size={{ md: 6, xs: 12 }}>
                    <CustomTextField disabled size='small' label={'Team Name'} value={teamName || ""} />
                </Grid>
                <Grid size={{ md: 6, xs: 12 }}>
                    <CustomTextField disabled size='small' label={'Email Address'} value={email || ""} />
                </Grid>
            </Grid>
            <Box display={'flex'} mt={1} sx={{ justifyContent: { xs: 'center', md: 'end' } }}>
                <CustomButton disabled>
                    Save changes
                </CustomButton>
            </Box>
        </>
    )
}

export default ChangeProfileInformationSection