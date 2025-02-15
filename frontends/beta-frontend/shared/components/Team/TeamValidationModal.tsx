import { Box, Typography } from '@mui/material';
import React from 'react';
import DoneIcon from '@mui/icons-material/Done';
import CloseIcon from '@mui/icons-material/Close';
import CustomModal from '../CustomModal';
import CloseButton from '../Common/CloseButton';

interface TeamValidationModalProps {
  teamFormationValidation: any;
  open: boolean;
  handleCloseModal: () => void;
}

const TeamValidationModal: React.FC<TeamValidationModalProps> = ({
  teamFormationValidation,
  open,
  handleCloseModal,
}) => {
  return (
    <CustomModal open={open} onClose={handleCloseModal}>
      <CloseButton handleCloseModal={handleCloseModal} />
      <div
        style={{
          borderRadius: '8px',
          padding: '10px',
          maxHeight: '100%',
          overflowY: 'auto',
          background: 'white',
        }}>
        {teamFormationValidation?.items?.map((error: any, index: number) => (
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              width: '100%',
            }}
            key={index}>
            <Typography>{error.message}</Typography>
            <Box
              sx={{
                color: error.valid ? 'green' : 'red',
              }}>
              {error.valid ? <DoneIcon /> : <CloseIcon />}
            </Box>
          </Box>
        ))}
      </div>
    </CustomModal>
  );
};

export default TeamValidationModal;
