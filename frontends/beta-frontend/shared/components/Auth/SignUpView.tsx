import { useForm } from 'react-hook-form';
import AuthViewWrapper from './AuthViewWrapper';
import { CircularProgress, Typography } from '@mui/material';
import AuthFormCard from './AuthFormCard';
import { useAuth } from 'hooks/useAuth';
import CustomButton from '../Common/CustomButton';
import StyledLink from '../Common/StyledLink';
import AuthTextField from './AuthTextField';

interface SignUpFormValues {
    email: string;
    password: string;
    confirmPassword: string;
    teamName: string;
}

const SignUpView = () => {

    const { apiSignUp, loading } = useAuth();

    const { handleSubmit, control, watch } = useForm<SignUpFormValues>({
        mode: 'onBlur',
        reValidateMode: 'onChange',
    });

    const handleRegister = (data: SignUpFormValues) => {
        apiSignUp(data.email, data.password, data.confirmPassword, data.teamName)
    };

    return (
        <AuthViewWrapper title="Sign Up">
            <img src={'/img/loginlogo.png'} height={'70px'} alt='' />
            <AuthFormCard>
                <form onSubmit={handleSubmit(handleRegister)}>
                    <AuthTextField
                        name={'email'}
                        control={control}
                    />
                    <AuthTextField
                        name={'teamName'}
                        control={control}
                    />
                    <AuthTextField
                        name={'password'}
                        type={'password'}
                        control={control}
                    />
                    <AuthTextField
                        name={'confirmPassword'}
                        type={'password'}
                        control={control}
                    />
                    <div style={{ display: 'flex', justifyContent: 'center' }}>
                        {loading ? <CircularProgress sx={{ color: '#FF3F84' }} /> :
                            <CustomButton type='submit' sx={{ width: '100%' }}>
                                Sign Up
                            </CustomButton>}
                    </div>
                </form>
                <Typography sx={{ color: '#808080', fontSize: '14px' }} textAlign={'center'} mt={'16px'}>
                    Have an account? {" "}
                    <StyledLink href="/signin">
                        Sign In
                    </StyledLink>
                </Typography>
            </AuthFormCard>
        </AuthViewWrapper>
    );
};

export default SignUpView;
