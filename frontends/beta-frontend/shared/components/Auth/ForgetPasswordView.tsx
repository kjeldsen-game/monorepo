import AuthViewWrapper from './AuthViewWrapper'
import AuthFormCard from './AuthFormCard'
import { Box, CircularProgress } from '@mui/material'
import CustomButton from '../Common/CustomButton'
import { useAuth } from 'hooks/useAuth'
import CustomTextField from '../Common/CustomTextField'
import { useRef } from 'react'

export interface ForgetPasswordForm {
    email: string;
}

interface ForgetPasswordViewProps { }

const ForgetPasswordView: React.FC<ForgetPasswordViewProps> = ({ }) => {

    const { useForgetPassword, loading } = useAuth()
    const emailRef = useRef<HTMLInputElement>(null);


    const handleButtonClick = () => {
        const request: ForgetPasswordForm = {
            email: emailRef.current?.value
        }
        useForgetPassword(request)
    }

    return (
        <AuthViewWrapper title={'Forget Password'}>
            <img src={'/img/loginlogo.png'} height={'150px'} alt='' />
            <AuthFormCard>
                <CustomTextField
                    size={'small'}
                    label={'Email'}
                    name={'email'}
                    inputRef={emailRef}
                />
                <Box display={'flex'} paddingTop={2} style={{ justifyContent: 'center' }}>
                    {loading ? <CircularProgress sx={{ color: '#FF3F84' }} /> :
                        <CustomButton onClick={handleButtonClick} sx={{ width: '100%' }}>
                            Send Reset Link
                        </CustomButton>}
                </Box>
            </AuthFormCard>
        </AuthViewWrapper>
    )
}

export default ForgetPasswordView