import { Box, CardHeader, } from '@mui/material';
;
import ProfileCard from './ProfileCard';
import AvatarSection from './AvatarSection';
import DashboardLink from '@/shared/components/DashboardLink';
import { useProfile } from 'modules/auth/hooks/useProfile';
import ChangeProfileInfoForm from './forms/ChangeProfileInfoForm';
import ChangePasswordForm from './forms/ChangePasswordForm';
import CustomCardHeader from '@/shared/components/texts/card-header/CustomCardHeader';

interface ProfileViewProps { }

const ProfileView: React.FC<ProfileViewProps> = ({ }) => {
    const { data } = useProfile()

    return (
        <Box sx={{ width: '100%' }}>
            <ProfileCard>
                <CustomCardHeader sx={{ fontSize: '24px' }}>Profile Information</CustomCardHeader>
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
