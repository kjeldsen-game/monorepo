import { Box, Dialog, DialogContent, DialogTitle, Typography } from '@mui/material'
import React from 'react'
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import CustomButton from '@/shared/components/Common/CustomButton';
import { PlayerSkill } from '@/shared/models/player/PlayerSkill';
import { useTrainingApi } from 'modules/player/hooks/api/useTrainingApi';

interface TrainingDialogProps {
    skillToTrain: PlayerSkill;
    skillUnderTraining: PlayerSkill | undefined;
    playerId: string;
    open: boolean;
    handleClose: () => void;
}

const TrainingDialog: React.FC<TrainingDialogProps> = ({ open, handleClose, skillToTrain, skillUnderTraining, playerId }) => {

    const { handleScheduleTrainingRequest } = useTrainingApi();

    const handleCloseModal = () => {
        handleClose();
    };

    const handleButtonClick = async () => {
        handleScheduleTrainingRequest({ skill: skillToTrain }, playerId);
        handleCloseModal();
    };

    return (
        <Dialog open={open} onClose={handleClose} scroll={'body'} maxWidth={'md'}>
            <DialogTitle
                display={'flex'}
                flexDirection={'column'}
                alignItems={'center'}
                justifyContent={'center'}>
                <FitnessCenterIcon fontSize="large" sx={{ color: '#FF3F84' }} />
                <Typography
                    variant="h6"
                    component="div"
                    sx={{ fontSize: '20px' }}
                    textAlign={'center'}>
                    Schedule Training
                </Typography>
            </DialogTitle>
            <DialogContent>
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
                    }}>
                </Box>
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

                <CustomButton sx={{ mt: 2, width: '100%' }} onClick={handleButtonClick}>
                    Schedule Traininig
                </CustomButton>
            </DialogContent>
        </Dialog>
    )
}

export default TrainingDialog