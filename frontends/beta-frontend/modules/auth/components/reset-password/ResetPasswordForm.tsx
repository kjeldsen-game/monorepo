import CustomButton from '@/shared/components/Common/CustomButton'
import PasswordTextField from '@/shared/components/Common/PasswordTextField'
import { CircularProgress } from '@mui/material'
import { Box } from '@mui/system'
import { useAuth } from 'modules/auth/hooks/useAuth'
import { PasswordResetRequest } from 'modules/auth/types/requests/authRequests'
import { useSearchParams } from 'next/navigation'
import React, { useRef } from 'react'
import PasswordInput from '../common/PasswordInput'
import { useForm } from 'react-hook-form'

interface ResetPasswordFormProps { }

type PasswordResetForm = FormData & {
    newPassword: string;
    confirmPassword: string;
};


const ResetPasswordForm: React.FC<ResetPasswordFormProps> = ({ }) => {
    const searchParams = useSearchParams();
    const token = searchParams.get("token");
    const { handleResetPassword, loading } = useAuth();

    const onSubmit = (data: PasswordResetForm) => {
        const request: PasswordResetRequest = {
            token,
            newPassword: data.newPassword,
            confirmPassword: data.confirmPassword,
        };
        handleResetPassword(request);
    };

    const { handleSubmit, control } = useForm<PasswordResetForm>({
        mode: 'onBlur',
        reValidateMode: 'onChange',
    });

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <PasswordInput
                name="newPassword"
                label='New Password'
                control={control}
                rules={{ required: "New Password is required!" }}
            />
            <PasswordInput
                name="confirmPassword"
                label='Confirm Password'
                control={control}
                rules={{ required: "Confirm Password is required!" }}
            />
            <Box display={'flex'} paddingTop={2} style={{ justifyContent: 'center' }}>
                {loading ? <CircularProgress sx={{ color: '#FF3F84' }} /> :
                    <CustomButton type='submit' sx={{ width: '100%' }}>
                        Reset Password
                    </CustomButton>}
            </Box>
        </form>
    )
}

export default ResetPasswordForm