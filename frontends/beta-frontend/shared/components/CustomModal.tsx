import * as React from 'react';
import Backdrop from '@mui/material/Backdrop';
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import Fade from '@mui/material/Fade';
import Typography from '@mui/material/Typography';
import { ReactNode } from 'react';
import { SxProps } from '@mui/material';

interface CustomModalProps {
  open: boolean;
  onClose: () => void;
  title?: string;
  description?: string;
  actions?: ReactNode;
  children?: ReactNode;
  sx?: SxProps;
}

const CustomModal: React.FC<CustomModalProps> = ({
  open,
  onClose,
  title,
  description,
  actions,
  children,
  sx = {},
}) => {
  return (
    <Modal open={open} onClose={onClose}>
      <Box
        sx={{
          position: 'fixed',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          border: '1px solid #DEE2E6',
          borderRadius: '8px',
          boxShadow: 24,
          backgroundColor: '#FFFFFF',
          p: 4,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          ...sx,
        }}>
        <Box>
          {title && (
            <Typography variant="h6" component="h2">
              {title}
            </Typography>
          )}
          {description && <Typography sx={{ mb: 2 }}>{description}</Typography>}
        </Box>
        {children}
        {actions && <Box sx={{ mt: 2 }}>{actions}</Box>}
      </Box>
    </Modal>
  );
};

export default CustomModal;
