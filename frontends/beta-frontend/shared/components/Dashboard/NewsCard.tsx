import { Card, CardActions, CardContent, CardMedia, Typography } from '@mui/material'
import React from 'react'
import CustomButton from '../Common/CustomButton'
import ArrowRightAltIcon from '@mui/icons-material/ArrowRightAlt';
import { theme } from '@/libs/material/theme';

const NewsCard = ({ id, openModal }) => {
  return (
    <Card sx={{ boxShadow: 0, border: '1px solid', borderColor: theme.palette.tertiary.main }}>
      <CardMedia
        component="img"
        height="140"
        image="https://placehold.co/600x400.png"
        alt="card image"
      />
      <CardContent sx={{ padding: " 16px !important" }}>
        <Typography variant="h6" fontSize={'16px'} fontWeight={'bold'}>Card with Image {id}</Typography>
        <Typography fontSize={'14px'} sx={{ color: theme.palette.quaternary.main }}>Some content under the image.</Typography>
        <CardActions sx={{ paddingX: 0 }}>
          <CustomButton onClick={() => openModal(true)} variant='outlined' sx={{ fontWeight: 'bold', border: 'none', '&:hover': {} }}>
            Read More
            <ArrowRightAltIcon />
          </CustomButton>
        </CardActions>
      </CardContent>
    </Card>
  )
}

export default NewsCard