import { Alert, Box, Typography } from '@mui/material';

import DashboardLink from '../DashboardLink';

import { useSession } from 'next-auth/react';
import { useProfileRepository } from '@/pages/api/profile/useProfileRepository';
import ChangeProfileInformationSection from './ChangeProfileInformationSection';
import ChangePasswordSection from './ChangePasswordSection';
import ProfileCard from './ProfileCard';
import AvatarSection from './AvatarSection';

interface ProfileViewProps {

}

const ProfileView: React.FC<ProfileViewProps> = ({

}) => {
    const { data: userData } = useSession();
    const { data } = useProfileRepository(userData?.accessToken)

    return (
        <Box sx={{ width: '100%' }}>
            <DashboardLink children={'Back to Dashboard'} />

            <Alert sx={{ mb: '16px' }} severity="warning">
                Please note: the app is not continuously deployed, so auction end times may behave unexpectedly.
                That said, the bidding system is fully functional and ready for beta testing — you can place bids on players successfully.
            </Alert>
            <ProfileCard>
                <Typography variant='h5' sx={{ fontWeight: 'bold' }}>
                    Profile Information
                </Typography>
                <AvatarSection avatar={data?.avatar} />
                <ChangeProfileInformationSection teamName={data?.teamName} email={data?.email} />
            </ProfileCard>
            <ProfileCard sx={{ mt: 2 }}>
                <ChangePasswordSection />
            </ProfileCard >
        </Box >
    );
};

export default ProfileView;
