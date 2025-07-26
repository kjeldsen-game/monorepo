import { Alert, Box, Typography } from '@mui/material';
;
import ProfileCard from './ProfileCard';
import AvatarSection from './AvatarSection';
import DashboardLink from '@/shared/components/DashboardLink';
import { useProfile } from 'modules/auth/hooks/useProfile';
import ChangeProfileInfoForm from './forms/ChangeProfileInfoForm';
import ChangePasswordForm from './forms/ChangePasswordForm';

interface ProfileViewProps { }

const ProfileView: React.FC<ProfileViewProps> = ({ }) => {
    const { data } = useProfile()

    return (
        <Box sx={{ width: '100%' }}>
            <DashboardLink children={'Back to Dashboard'} />
            <Alert sx={{ mb: '16px' }} severity="warning">
                Please note the current app status:
                <ul>
                    <li>✅ Avatar upload and delete functionality is fully working.</li>
                    <li>✅ Password change functionality is fully working.</li>
                    <li>⚠️ Profile information update is currently disabled.</li>
                </ul>
            </Alert>

            <ProfileCard>
                <Typography variant='h5' sx={{ fontWeight: 'bold' }}>
                    Profile Information
                </Typography>
                <AvatarSection avatar={data?.avatar} />
                <ChangeProfileInfoForm teamName={data?.teamName} email={data?.email} />
            </ProfileCard>
            <ProfileCard sx={{ mt: 2 }}>
                <ChangePasswordForm />
            </ProfileCard >
        </Box >
    );
};

export default ProfileView;
