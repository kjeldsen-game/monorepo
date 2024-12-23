import { Alert, AlertColor, Snackbar } from '@mui/material';
import React from 'react';

interface SnackbarAlertProps {
  handleClose: () => void;
  open: boolean;
  type: AlertColor;
  message: string;
}

const SnackbarAlert: React.FC<SnackbarAlertProps> = ({
  handleClose,
  open,
  type,
  message,
}) => {
  return (
    <Snackbar
      onClose={handleClose}
      autoHideDuration={1500}
      open={open}
      anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
      <Alert severity={type} sx={{ width: '100%' }}>
        {message}
      </Alert>
    </Snackbar>
  );
};

export default SnackbarAlert;
