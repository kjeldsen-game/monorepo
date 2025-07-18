import { Avatar, Box, Typography } from '@mui/material'
import CustomButton from '../Common/CustomButton'
import DeleteIcon from '@mui/icons-material/Delete';
import { useError } from '@/shared/contexts/ErrorContext';
import { useProfileRepository } from '@/pages/api/profile/useProfileRepository';
import HiddenInput from '../Common/HiddenInput';
import { useSession } from 'next-auth/react';

interface AvatarSectionProps {
    avatar?: string;
}

const AvatarSection: React.FC<AvatarSectionProps> = ({ avatar }) => {
    const { data: userData } = useSession();
    const { setError } = useError();
    const { changeAvatar } = useProfileRepository(userData?.accessToken);

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
        changeAvatar(formData);
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
                <Box paddingY={1} display={'flex'}>
                    <CustomButton
                        component="label"
                        role={undefined}
                        variant="contained"
                        tabIndex={-1}
                        sx={{ marginRight: '8px' }}>
                        Change picture
                        <HiddenInput
                            data-testid="avatar-upload-input"
                            type="file"
                            onChange={handleAvatarChange} />
                    </CustomButton>
                    <CustomButton variant='outlined' onClick={(event) => handleAvatarChange(event)}>
                        <DeleteIcon />
                    </CustomButton>
                </Box>
            </Box>
        </Box>
    )
}

export default AvatarSection