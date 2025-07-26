import CustomButton from '@/shared/components/Common/CustomButton'
import HiddenInput from '@/shared/components/Common/HiddenInput'
import { Box } from '@mui/material'
import DeleteIcon from '@mui/icons-material/Delete';
import CustomIconButton from '@/shared/components/Common/CustomIconButton';

interface AvatarFormProps {
    handleAvatarChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const AvatarForm: React.FC<AvatarFormProps> = ({ handleAvatarChange }) => {
    return (
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
            <CustomIconButton onClick={(event) => handleAvatarChange(event)}>
                <DeleteIcon />
            </CustomIconButton>
        </Box>
    )
}

export default AvatarForm