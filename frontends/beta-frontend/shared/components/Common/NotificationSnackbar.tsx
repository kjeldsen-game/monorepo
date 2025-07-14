import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';
import { Alert, Snackbar } from '@mui/material';

const NotificationSnackbar = () => {
    const { notification, setNotification } = useNotification();

    return (

        <Snackbar open={!!notification} autoHideDuration={1500} onClose={() => setNotification(null)} message={notification} >
            <Alert severity={'success'} sx={{ width: '100%' }}>
                {notification}
            </Alert>
        </Snackbar>
    );
};

export default NotificationSnackbar;
