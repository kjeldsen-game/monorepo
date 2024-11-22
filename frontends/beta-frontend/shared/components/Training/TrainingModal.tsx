import * as React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import LocalAtmIcon from '@mui/icons-material/LocalAtm';
import { IconButton, TextField } from '@mui/material';
import { useSession } from 'next-auth/react';
import CustomModal from '../CustomModal';
import VerifiedIcon from '@mui/icons-material/Verified';
import { CloseOutlined } from '@mui/icons-material';
import { PlayerSkill } from '@/shared/models/PlayerSkill';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import MarketButton from '../Market/MarketButton';
import { useScheduledTrainingRepository } from '@/pages/api/training/useScheduledTrainingRepository';

interface TrainingModalProps {
  skillToTrain: PlayerSkill;
  skillUnderTraining: PlayerSkill | undefined;
  playerToTrain: string;
  open: boolean;
  handleClose: () => void;
}

const TrainingModal: React.FC<TrainingModalProps> = ({
  skillToTrain,
  skillUnderTraining,
  playerToTrain,
  open,
  handleClose,
}: TrainingModalProps) => {
  const { data: userData, status: sessionStatus } = useSession({
    required: true,
  });

  const { scheduleTraining } = useScheduledTrainingRepository(
    userData?.user.teamId,
    userData?.accessToken,
    playerToTrain,
  );

  const handleCloseModal = () => {
    handleClose();
  };

  const handleButtonClick = async () => {
    scheduleTraining(skillToTrain);
    handleCloseModal();
  };

  return (
    <>
      <CustomModal
        sx={{ width: '400px' }}
        onClose={handleCloseModal}
        open={open}>
        <Box>
          <IconButton
            sx={{
              width: '24px',
              height: '24px',
              position: 'absolute',
              left: '90%',
              top: '3%',
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
          <FitnessCenterIcon fontSize="large" sx={{ color: '#FF3F84' }} />
        </Box>
        <Typography
          variant="h6"
          component="h2"
          sx={{ fontSize: '20px' }}
          textAlign={'center'}>
          Schedule Training
        </Typography>
        <Typography
          variant="body2"
          sx={{
            color: '#54595E99',
            padding: '8px',
            borderRadius: '4px',
            textAlign: 'center',
          }}>
          Lorem ipsum que tu quieras para explicar como es el proceso de compra
          de un jugador.
        </Typography>

        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            width: '100%',
          }}></Box>
        <Box sx={{ textAlign: 'center' }}>
          {' '}
          {skillUnderTraining != undefined ? (
            <Typography>
              Are you sure that you want to override your current older
              scheduled training of{' '}
              {convertSnakeCaseToTitleCase(skillUnderTraining)} for{' '}
              {convertSnakeCaseToTitleCase(skillToTrain)}?
            </Typography>
          ) : (
            <Typography>
              Are you sure that you want to schedule training for{' '}
              {convertSnakeCaseToTitleCase(skillToTrain)}?
            </Typography>
          )}
        </Box>

        <MarketButton sx={{ mt: 2, width: '100%' }} onClick={handleButtonClick}>
          Schedule Traininig
        </MarketButton>
      </CustomModal>
    </>
  );
};

export default TrainingModal;
