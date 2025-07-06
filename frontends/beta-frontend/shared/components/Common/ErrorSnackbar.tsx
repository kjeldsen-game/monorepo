import { useError } from '@/shared/contexts/ErrorContext';
import { Snackbar } from '@mui/material';

const ErrorSnackbar = () => {
    const { error, setError } = useError();

    return (
        <Snackbar open={!!error} autoHideDuration={1500} onClose={() => setError(null)} message={error} />
    );
};

export default ErrorSnackbar;
