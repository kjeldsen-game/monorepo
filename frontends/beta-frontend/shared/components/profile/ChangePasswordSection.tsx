import { Box, Grid, Typography } from '@mui/material'
import { useRef } from 'react'
import PasswordTextField from '../Common/PasswordTextField'
import CustomButton from '../Common/CustomButton'
import { ChangePasswordRequest, useProfileRepository } from '@/pages/api/profile/useProfileRepository'
import { useSession } from 'next-auth/react'

interface ChangePasswordSectionProps { }

const ChangePasswordSection: React.FC<ChangePasswordSectionProps> = () => {
    const { data: userData } = useSession();
    const { changePassword } = useProfileRepository(userData?.accessToken)

    const currentRef = useRef<HTMLInputElement>(null);
    const newRef = useRef<HTMLInputElement>(null);
    const confirmRef = useRef<HTMLInputElement>(null);

    const handleChangePasswordButton = async () => {
        const request: ChangePasswordRequest = {
            oldPassword: currentRef.current?.value,
            newPassword: newRef.current?.value,
            confirmPassword: confirmRef.current?.value
        }
        changePassword(request)
    }

    return (
        <>
            <Typography variant='h5' sx={{ fontWeight: 'bold' }}>
                Password Management
            </Typography>
            <Grid container sx={{ width: '100%', my: '16px' }} spacing={2}>
                <Grid size={{ md: 4, xs: 12 }}>
                    <PasswordTextField size='small' label='Current Password' inputRef={currentRef} />
                </Grid >
                <Grid size={{ md: 4, xs: 12 }}>
                    <PasswordTextField size='small' label='New Password' inputRef={newRef} />
                </Grid>
                <Grid size={{ md: 4, xs: 12 }}>
                    <PasswordTextField size='small' label='Confirm Password' inputRef={confirmRef} />
                </Grid>
            </Grid>
            <Box display={'flex'} mt={1} sx={{ justifyContent: { xs: 'center', md: 'end' } }}>
                <CustomButton onClick={handleChangePasswordButton}>
                    Change Password
                </CustomButton>
            </Box>
        </>
    )
}

export default ChangePasswordSection