import { CloseOutlined } from '@mui/icons-material';
import { IconButton } from '@mui/material';
import React from 'react';

interface CloseButtonProps {
  handleCloseModal: () => void;
}

const CloseButton: React.FC<CloseButtonProps> = ({ handleCloseModal }) => {
  return (
    <IconButton
      sx={{
        width: '24px',
        height: '24px',
        position: 'absolute',
        left: 'calc(100% - 24px - 5px)',
        top: 'calc(0% + 5px)',
        background: '#E5E5E5',
      }}
      onClick={handleCloseModal}
      aria-label="close">
      <CloseOutlined
        sx={{
          color: '#4F4F4F',
          width: '15px',
          height: '15px',
        }}
      />
    </IconButton>
  );
};

export default CloseButton;
