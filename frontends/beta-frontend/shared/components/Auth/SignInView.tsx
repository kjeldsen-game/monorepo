import AuthViewWrapper from './AuthViewWrapper'
import AuthTextField from './AuthTextField'
import { CircularProgress, Typography } from '@mui/material'
import { useForm } from 'react-hook-form'
import AuthFormCard from './AuthFormCard'
import { useAuth } from 'hooks/useAuth'
import StyledLink from '../Common/StyledLink'
import CustomButton from '../Common/CustomButton'


interface SignInFormValues {
    email: string;
    password: string;
}

const SignInView = () => {

    const { apiSignIn, loading } = useAuth();

    const { handleSubmit, control } = useForm<SignInFormValues>({
        mode: 'onBlur',
        reValidateMode: 'onChange',
    });

    const handleLogin = (data: SignInFormValues) => {
        apiSignIn(data.email, data.password)
    };

    return (
        <AuthViewWrapper title="Sign In">
            <img src={'/img/loginlogo.png'} height={'150px'} alt='' />
            <AuthFormCard>
                <form
                    onSubmit={handleSubmit(handleLogin)}
                >
                    <AuthTextField
                        name={'email'}
                        control={control}
                    />
                    <AuthTextField
                        name={'password'}
                        type={'password'}
                        control={control}
                    />
                    <div style={{ display: 'flex', justifyContent: 'center' }}>
                        {loading ? <CircularProgress sx={{ color: '#FF3F84' }} /> :
                            <CustomButton type='submit' sx={{ width: '100%' }}>
                                Sign In
                            </CustomButton>}
                    </div>
                </form>
                <Typography sx={{ color: '#808080', fontSize: '14px' }} textAlign={'center'} mt={'16px'}>
                    No account? {" "}
                    <StyledLink href='/signup'>
                        Sign Up
                    </StyledLink>
                </Typography>
            </AuthFormCard>
            <StyledLink href='/' mt={'16px'}>
                Forgot password?
            </StyledLink>

        </AuthViewWrapper>
    )
}

export default SignInView