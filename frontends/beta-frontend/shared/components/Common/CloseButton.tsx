import { CloseOutlined } from '@mui/icons-material';
import { IconButton, SxProps } from '@mui/material';
import React from 'react';

type CloseButtonVariant = "default" | "filled"
interface CloseButtonProps {
  handleCloseModal: (event?: React.MouseEvent<HTMLButtonElement>) => void;
  variant?: CloseButtonVariant;
  sx?: SxProps;
}

const CloseButton: React.FC<CloseButtonProps> = ({ handleCloseModal, variant = "default", sx }) => {

  return (
    <IconButton
      sx={{
        width: '24px',
        height: '24px',
        position: 'absolute',
        left: 'calc(100% - 24px - 5px)',
        top: 'calc(0% + 5px)',
        background: variant === "default" ? '#E5E5E5' : '#FF3F84',
        '&:hover': {
          background: variant === "default" ? '#E5E5E5' : '#FF3F84',
          boxShadow: 'none',
        },
        ...sx
      }}
      onClick={handleCloseModal}
      aria-label="close">
      <CloseOutlined
        sx={{
          color: variant === "default" ? '#4F4F4F' : 'white',
          width: '15px',
          height: '15px',
        }}
      />
    </IconButton>
  );
};

export default CloseButton;
