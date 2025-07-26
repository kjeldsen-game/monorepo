import { Avatar, Box, Typography } from '@mui/material'
import { useError } from '@/shared/contexts/ErrorContext';
import { useProfile } from 'modules/auth/hooks/useProfile';
import AvatarForm from './forms/AvatarForm';

interface AvatarSectionProps {
    avatar?: string;
}

const AvatarSection: React.FC<AvatarSectionProps> = ({ avatar }) => {
    const { setError } = useError();
    const { handleChangeAvatar } = useProfile();

    const handleAvatarChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        const formData = new FormData();
        if (file) {
            const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
            if (!allowedTypes.includes(file.type)) {
                setError('Please select a JPG, PNG, or GIF image.');
                return;
            }
            const reader = new FileReader();
            reader.readAsDataURL(file);
            formData.append('file', file);
        } else {
            formData.append('file', new Blob([], { type: 'image/png' }));
        }
        handleChangeAvatar(formData);
    }

    return (
        <Box
            display={'flex'}
            alignItems={'center'}
            padding={2}>
            <Avatar
                sx={{ width: '100px', height: '100px', border: '1px solid #FF3F84' }}
                alt="Upload new avatar"
                src={avatar
                    ? `data:image/jpeg;base64,${avatar}`
                    : undefined} />
            <Box
                sx={{ paddingLeft: '16px' }}>
                <Typography variant='h6' fontWeight={'bold'} paddingY={0.5}>
                    Profile Picture
                </Typography>
                <Typography variant='body2' color="text.secondary">
                    PNG, JPG, GIF up to 10 mb
                </Typography>
                <AvatarForm
                    handleAvatarChange={handleAvatarChange}
                />
            </Box>
        </Box>
    )
}

export default AvatarSection