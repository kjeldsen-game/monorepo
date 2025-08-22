import { Box, Dialog, DialogContent, Typography } from '@mui/material'
import React from 'react'
import CloseButton from '@/shared/components/Common/CloseButton';
import DoneIcon from '@mui/icons-material/Done';
import CloseIcon from '@mui/icons-material/Close';
import { LineupValidationResponse } from 'modules/player/types/Responses';
import { LineupValidationItem } from 'modules/player/types/Team';

interface TeamValidationDialogProps {
    open: boolean;
    handleClose: () => void;
    teamValidation: LineupValidationResponse;
}

const TeamValidationDialog: React.FC<TeamValidationDialogProps> = ({ open, handleClose, teamValidation }) => {

    return (
        <Dialog open={open} onClose={handleClose} scroll={'body'} maxWidth={'xs'}>
            <CloseButton handleCloseModal={handleClose} />
            <DialogContent>
                <Box
                    style={{
                        borderRadius: '8px',
                        padding: 8,
                        maxHeight: '100%',
                        overflowY: 'auto',
                    }}>
                    {teamValidation?.items?.map((error: LineupValidationItem, index: number) => (
                        <Box
                            sx={{
                                display: 'flex',
                                justifyContent: 'space-between',
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
                </Box>
            </DialogContent>
        </Dialog >
    )
}

export default TeamValidationDialog