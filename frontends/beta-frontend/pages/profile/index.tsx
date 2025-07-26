import type { NextPage } from 'next';
import { Box } from '@mui/material';
import ProfileView from 'modules/auth/components/profile/ProfileView';

interface ProfileProps { }

const Profile: NextPage<ProfileProps> = ({ }) => {
    return (
        <>
            <Box>
                <Box
                    sx={{
                        display: 'flex',
                        marginBottom: '2rem',
                        alignItems: 'center',
                    }}>
                    <ProfileView />
                </Box>
            </Box>
        </>
    );
};

export default Profile;
