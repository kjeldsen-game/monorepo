import { CloseOutlined } from '@mui/icons-material';
import { IconButton } from '@mui/material';
import React from 'react';

interface CloseButtonProps {
  handleCloseModal: () => void;
  sx?: object;
}

const LineupCloseButton: React.FC<CloseButtonProps> = ({
  handleCloseModal,
  sx,
}) => {
  return (
    <IconButton
      sx={{
        width: '24px',
        height: '24px',
        position: 'absolute',
        left: 'calc(100% - 24px - 5px)',
        top: 'calc(0% + 5px)',
        background: '#FF3F84',
        '&:hover': {
          background: '#FF3F84',
          boxShadow: 'none',
        },
        ...sx,
      }}
      onClick={handleCloseModal}
      aria-label="close">
      <CloseOutlined
        sx={{
          color: 'white',
          width: '15px',
          height: '15px',
        }}
      />
    </IconButton>
  );
};

export default LineupCloseButton;
