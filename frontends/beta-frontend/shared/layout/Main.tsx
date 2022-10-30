import React, { FC, PropsWithChildren } from 'react'
import { Box } from '@mui/material'

export const Main: FC<PropsWithChildren> = ({ children }) => {
  return (
    <Box p={3} display="flex" component="main">
      {children}
    </Box>
  )
}
