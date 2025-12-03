import { Dialog, DialogContent, DialogTitle, Typography } from '@mui/material'
import React from 'react'
import CloseButton from '../Common/CloseButton'

interface NewsDialogProps {
  open: boolean;
  handleClose: () => void;
}

const NewsDialog = ({ open, handleClose }: NewsDialogProps) => {
  return (
    <Dialog open={open} onClose={handleClose} scroll={'body'} maxWidth={'xs'}>
      <CloseButton handleCloseModal={handleClose} />
      <DialogTitle display={'flex'} flexDirection={'column'} alignItems={'center'} justifyItems={'center'}>
        {/* <LocalAtmIcon fontSize="large" sx={{ color: '#FF3F84' }} /> */}
        <Typography variant="h6">
          NewsDialog
        </Typography>
      </DialogTitle>
      <DialogContent>
        <Typography variant="body2" color="text.secondary" textAlign={'center'}>
          Lorem ipsum que tu quieras para explicar como es el proceso de compra de un jugador
        </Typography>
       
      </DialogContent>
    </Dialog >
  )
}

export default NewsDialog