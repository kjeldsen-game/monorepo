import { useForm } from 'react-hook-form';
import { Box, CircularProgress, Typography } from '@mui/material';
import AuthViewWrapper from '../common/AuthViewWrapper';
import AuthFormCard from '../common/AuthFormCard';
import CustomButton from '@/shared/components/Common/CustomButton';
import PasswordInput from '../common/PasswordInput';
import StyledLink from '@/shared/components/Common/StyledLink';
import TextInput from '../common/TextInput';
import { useAuth } from 'modules/auth/hooks/useAuth';
import { SignUpRequest } from 'modules/auth/types/requests/authRequests';

interface SignUpFormValues {
    email: string;
    password: string;
    confirmPassword: string;
    teamName: string;
}

const SignUpView = () => {

    const { handleSignUp, loading } = useAuth();

    const { handleSubmit, control } = useForm<SignUpFormValues>({
        mode: 'onBlur',
        reValidateMode: 'onChange',
    });

    const onSubmit = (data: SignUpFormValues) => {
        const request: SignUpRequest = {
            email: data.email,
            teamName: data.teamName,
            password: data.password,
            confirmPassword: data.confirmPassword
        }
        handleSignUp(request)
    };

    return (
        <AuthViewWrapper title="Sign Up">
            <img src={'/img/loginlogo.png'} height={'150px'} alt='' />
            <AuthFormCard>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <TextInput
                        name={'email'}
                        label={"Email"}
                        control={control}
                    />
                    <TextInput
                        sx={{ my: 1 }}
                        name={'teamName'}
                        label={'Team Name'}
                        control={control}
                    />
                    <PasswordInput
                        sx={{ mb: 1 }}
                        name={'password'}
                        label={"Password"}
                        control={control}
                    />
                    <PasswordInput
                        name={'confirmPassword'}
                        label={"Confirm Password"}
                        control={control}
                    />
                    <Box display={'flex'} paddingTop={1} style={{ justifyContent: 'center' }}>
                        {loading ? <CircularProgress sx={{ color: '#FF3F84' }} /> :
                            <CustomButton type='submit' sx={{ width: '100%' }}>
                                Sign Up
                            </CustomButton>}
                    </Box>
                </form>
                <Typography sx={{ color: '#808080', fontSize: '14px' }} textAlign={'center'} mt={'16px'}>
                    Have an account? {" "}
                    <StyledLink href="/auth/signin">
                        Sign In
                    </StyledLink>
                </Typography>
            </AuthFormCard>
        </AuthViewWrapper>
    );
};

export default SignUpView;
