import { Box, Grid, Typography } from '@mui/material'
import { useRef } from 'react'
import PasswordTextField from '@/shared/components/Common/PasswordTextField'
import CustomButton from '@/shared/components/Common/CustomButton'
import { useProfile } from 'modules/auth/hooks/useProfile'
import { ChangePasswordRequest } from 'modules/auth/types/requests/profileRequests'
import PasswordInput from '../../common/PasswordInput'
import { useForm } from 'react-hook-form'

interface ChangePasswordFormProps { }

type ChangePasswordForm = FormData & {
    oldPassword: string;
    newPassword: string;
    confirmPassword: string;
};

const ChangePasswordForm: React.FC<ChangePasswordFormProps> = () => {
    const { handleChangePassword } = useProfile();

    const {
        handleSubmit,
        control,
    } = useForm<ChangePasswordForm>({
        mode: 'onBlur',
        reValidateMode: 'onChange',
    });

    const onSubmit = (data: ChangePasswordForm) => {
        const request: ChangePasswordRequest = {
            oldPassword: data.oldPassword,
            newPassword: data.newPassword,
            confirmPassword: data.confirmPassword,
        };
        handleChangePassword(request);
    };

    return (
        <>
            <Typography variant='h5' sx={{ fontWeight: 'bold' }}>
                Password Management
            </Typography>
            <form onSubmit={handleSubmit(onSubmit)}>
                <Grid container sx={{ width: '100%', my: '16px' }} spacing={2}>
                    <Grid size={{ md: 4, xs: 12 }}>
                        <PasswordInput
                            name="oldPassword"
                            label='Current Password'
                            control={control}
                            rules={{ required: "Current Password is required!" }}
                        />
                    </Grid >
                    <Grid size={{ md: 4, xs: 12 }}>
                        <PasswordInput
                            name="newPassword"
                            label='New Password'
                            control={control}
                            rules={{ required: "New Password is required!" }}
                        />
                    </Grid>
                    <Grid size={{ md: 4, xs: 12 }}>
                        <PasswordInput
                            name="confirmPassword"
                            label='Confirm Password'
                            control={control}
                            rules={{ required: "Confirm Password is required!" }}
                        />
                    </Grid>
                </Grid>
                <Box display={'flex'} mt={1} sx={{ justifyContent: { xs: 'center', md: 'end' } }}>
                    <CustomButton
                        type='submit'>
                        Change Password
                    </CustomButton>
                </Box>
            </form>

        </>
    )
}

export default ChangePasswordForm