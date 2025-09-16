import { Box, Dialog, DialogContent, Step, StepButton, Stepper, Typography } from '@mui/material'
import React, { ReactNode } from 'react'
import CustomButton from './Common/CustomButton';
import CloseButton from './Common/CloseButton';
import { theme } from '@/libs/material/theme';
import { useStepperManager } from '../hooks/useStepperManager';

export type StepContent = {
    label: string;
    text: string;
    image?: string;
};

interface TutorialDialogProps {
    stepContent: StepContent[],
    open?: boolean,
    handleClose: () => void;
    children?: ReactNode;
}

const TutorialDialog: React.FC<TutorialDialogProps> = ({ stepContent, open, handleClose, children }) => {

    const { activeStep, handleNext, handleStep, completed } = useStepperManager(stepContent);

    return (
        <Dialog open={open} onClose={handleClose} scroll={'body'} maxWidth={'sm'}>
            <DialogContent>
                <CloseButton handleCloseModal={handleClose} />
                <Box>
                    <Stepper nonLinear activeStep={activeStep}
                        sx={{
                            '& .MuiStepIcon-root': {
                                color: theme.palette.tertiary.main,
                            },
                            '& .MuiStepIcon-root.Mui-active': {
                                color: theme.palette.secondary.main,
                            },
                            '& .MuiStepIcon-text': {
                                fill: 'black',
                            },
                            '& .MuiStepIcon-root.Mui-active .MuiStepIcon-text': {
                                fill: 'white',
                            },
                        }}>
                        {stepContent?.map((label, index) => (
                            <Step key={label.label} completed={completed[index]}>
                                <StepButton color="inherit" onClick={handleStep(index)}>
                                    {label.label}
                                </StepButton>
                            </Step>
                        ))}
                    </Stepper>
                    <Box padding={1}>
                        <Box paddingY={1}>
                            {/* {children} */}
                            <Typography>
                                {stepContent[activeStep].text}
                            </Typography>
                            {stepContent[activeStep].image && (
                                <Box mt={2} display="flex" justifyContent="center">
                                    <img
                                        src={stepContent[activeStep].image}
                                        alt={`Step ${activeStep + 1}`}
                                        style={{ maxWidth: '100%', borderRadius: 8, maxHeight: '200px' }}
                                    />
                                </Box>
                            )}
                        </Box>
                        <Box display={'flex'} justifyContent={'end'}>
                            <CustomButton onClick={handleNext}>
                                Next
                            </CustomButton>
                        </Box>
                    </Box>
                </Box>
            </DialogContent>
        </Dialog>
    )
}

export default TutorialDialog