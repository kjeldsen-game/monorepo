
import { Box, CircularProgress, Typography } from '@mui/material'
import { Controller, useForm } from 'react-hook-form'
import AuthViewWrapper from '../common/AuthViewWrapper';
import AuthFormCard from '../common/AuthFormCard';
import StyledLink from '@/shared/components/Common/StyledLink';
import CustomButton from '@/shared/components/Common/CustomButton';
import PasswordInput from '../common/PasswordInput';
import CustomTextField from '@/shared/components/Common/CustomTextField';
import { useAuth } from 'modules/auth/hooks/useAuth';
import { SignInRequest } from 'modules/auth/types/requests/authRequests';


interface SignInFormValues {
    email: string;
    password: string;
}

const SignInView = () => {
    const { loading, handleSignIn } = useAuth();
    const { handleSubmit, control } = useForm<SignInFormValues>({
        mode: 'onBlur',
        reValidateMode: 'onChange',
    });

    const onSubmit = (data: SignInFormValues) => {
        const request: SignInRequest = {
            email: data.email,
            password: data.password
        }
        handleSignIn(request)
    };

    return (
        <AuthViewWrapper title="Sign In">
            <img src={'/img/loginlogo.png'} height={'150px'} alt='' />
            <AuthFormCard>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <Controller
                        name={'email'}
                        control={control}
                        defaultValue={''}
                        rules={{ required: `Email is required!` }}
                        render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
                            <CustomTextField
                                value={value}
                                onChange={onChange}
                                onBlur={onBlur}
                                error={!!error}
                                helperText={error ? error.message : ''}
                                sx={{ mb: 1 }} name='email' />
                        )} />
                    <PasswordInput
                        name={'password'}
                        label={"Password"}
                        control={control}
                    />
                    <Box display={'flex'} paddingTop={1} style={{ justifyContent: 'center' }}>
                        {loading ? <CircularProgress sx={{ color: '#FF3F84' }} /> :
                            <CustomButton type='submit' sx={{ width: '100%' }}>
                                Sign In
                            </CustomButton>}
                    </Box>
                </form>
                <Typography sx={{ color: '#808080', fontSize: '14px' }} textAlign={'center'} mt={'16px'}>
                    No account? {" "}
                    <StyledLink href='/auth/signup'>
                        Sign Up
                    </StyledLink>
                </Typography>
            </AuthFormCard>
            <StyledLink href='/auth/forget-password' mt={'16px'}>
                Forgot password?
            </StyledLink>
        </AuthViewWrapper>
    )
}

export default SignInView