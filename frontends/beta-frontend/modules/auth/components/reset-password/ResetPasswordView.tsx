import AuthViewWrapper from '../common/AuthViewWrapper';
import AuthFormCard from '../common/AuthFormCard';
import ResetPasswordForm from './ResetPasswordForm';
import { Typography } from '@mui/material';
import StyledLink from '@/shared/components/Common/StyledLink';

export interface ResetPasswordForm {
    token: string;
    newPassword: string;
    confirmPassword: string
}

interface ResetPasswordViewProps { }

const ResetPasswordView: React.FC<ResetPasswordViewProps> = () => {

    return (
        <AuthViewWrapper title={'Forget Password'}>
            <img src={'/img/loginlogo.png'} height={'150px'} alt='' />
            <AuthFormCard>
                <ResetPasswordForm />
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

export default ResetPasswordView