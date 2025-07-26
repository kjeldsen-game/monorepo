
import { Box, CircularProgress, Typography } from '@mui/material'
import { useRef } from 'react'
import AuthViewWrapper from '../common/AuthViewWrapper';
import AuthFormCard from '../common/AuthFormCard';
import CustomButton from '@/shared/components/Common/CustomButton';
import CustomTextField from '@/shared/components/Common/CustomTextField';
import StyledLink from '@/shared/components/Common/StyledLink';
import { useAuth } from 'modules/auth/hooks/useAuth';

export interface ForgetPasswordForm {
    email: string;
}

interface ForgetPasswordViewProps { }

const ForgetPasswordView: React.FC<ForgetPasswordViewProps> = ({ }) => {

    const { handleForgetPassword, loading } = useAuth()
    const emailRef = useRef<HTMLInputElement>(null);


    const handleButtonClick = () => {
        const request: ForgetPasswordForm = {
            email: emailRef.current?.value
        }
        handleForgetPassword(request)
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
                <Typography sx={{ color: '#808080', fontSize: '14px' }} textAlign={'center'} mt={'16px'}>
                    Have an account? {" "}
                    <StyledLink href="/signin">
                        Sign In
                    </StyledLink>
                </Typography>
            </AuthFormCard>

        </AuthViewWrapper>
    )
}

export default ForgetPasswordView