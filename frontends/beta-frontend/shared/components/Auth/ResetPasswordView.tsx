import AuthViewWrapper from './AuthViewWrapper'
import AuthFormCard from './AuthFormCard'
import AuthTextField from './AuthTextField'
import { Box, CircularProgress } from '@mui/material'
import CustomButton from '../Common/CustomButton'
import { useForm } from 'react-hook-form'
import { useSearchParams } from 'next/navigation'
import { useAuth } from 'hooks/useAuth'
import PasswordTextField from '../Common/PasswordTextField'
import { useRef } from 'react'

export interface ResetPasswordForm {
    token: string;
    newPassword: string;
    confirmPassword: string
}

interface ResetPasswordViewProps { }

const ResetPasswordView: React.FC<ResetPasswordViewProps> = () => {

    const newRef = useRef<HTMLInputElement>(null);
    const confirmRef = useRef<HTMLInputElement>(null);

    const searchParams = useSearchParams();
    const token = searchParams.get("token");
    const { useResetPassword, loading } = useAuth();

    const handleChangePasswordButton = async () => {
        const request: ResetPasswordForm = {
            token: token,
            newPassword: newRef.current?.value,
            confirmPassword: confirmRef.current?.value
        }
        useResetPassword(request)
    }
    return (
        <AuthViewWrapper title={'Forget Password'}>
            <img src={'/img/loginlogo.png'} height={'150px'} alt='' />
            <AuthFormCard>
                <PasswordTextField
                    inputRef={newRef}
                    label={'New Password'}
                    name={'newPassowrd'}
                />
                <PasswordTextField
                    inputRef={confirmRef}
                    sx={{ mt: 2 }}
                    label={'Confirm Password'}
                    name={'confirmPassword'}
                />
                <Box display={'flex'} paddingTop={2} style={{ justifyContent: 'center' }}>
                    {loading ? <CircularProgress sx={{ color: '#FF3F84' }} /> :
                        <CustomButton onClick={handleChangePasswordButton} sx={{ width: '100%' }}>
                            Reset Password
                        </CustomButton>}
                </Box>
            </AuthFormCard>
        </AuthViewWrapper>
    )
}

export default ResetPasswordView