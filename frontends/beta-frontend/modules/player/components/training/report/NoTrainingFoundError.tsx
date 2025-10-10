import AssignmentOutlinedIcon from '@mui/icons-material/AssignmentOutlined';
import { Typography } from '@mui/material';
import { Box } from '@mui/system';
import { theme } from '@/libs/material/theme';

const NoTrainingFoundError = () => {
    return (
        <Box padding={2} display={'flex'} flexDirection={'column'} justifyContent={'center'} alignItems={'center'}>
            <AssignmentOutlinedIcon sx={{ fontSize: '50px', color: theme.palette.quaternary.main }} />
            <Typography variant='h6' fontWeight={'bold'}>
                No Training History Found
            </Typography>
            <Typography textAlign={'center'} variant='subtitle2' sx={{ color: theme.palette.quaternary.main }} >
                It looks like your team hasn't completed any training yet. Once they do, you'll see reports here.
            </Typography>
        </Box>
    )
}

export default NoTrainingFoundError