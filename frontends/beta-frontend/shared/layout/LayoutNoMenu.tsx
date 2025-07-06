import React, { ReactNode } from 'react'
import { Layout } from './Layout'
import { Box } from '@mui/material'

interface LayoutNoMenuProps {
    children: ReactNode;
}

const LayoutNoMenu: React.FC<LayoutNoMenuProps> = ({ children }) => {
    return (
        <Box
            sx={{
                position: 'relative',
                width: '100vw',
                height: '100vh',
                overflow: 'hidden',
            }}>
            <Box
                sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    width: { lg: '95vw', md: '95vw', sm: '160vw', xs: '95vh', },
                    height: { lg: '95vh', md: '95vh', sm: '140vw', xs: '95vh', },
                    backgroundImage:
                        'linear-gradient(rgba(255,255,255,0.9), rgba(255,255,255,0.9)), url("/img/testss.svg")',
                    backgroundRepeat: 'no-repeat',
                    backgroundPosition: 'center',
                    backgroundSize: 'contain',
                    transformOrigin: 'center center',
                    transform: {
                        xs: 'translate(-50%, -50%) rotate(90deg)',
                        md: 'translate(-50%, -50%) rotate(0deg)',
                    },
                    zIndex: 1,
                }}
            />
            <Box
                sx={{
                    position: 'relative',
                    zIndex: 10,
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    height: '100%',
                    width: '100%',
                }}>
                {children}
            </Box>
        </Box>
    )
}

export default LayoutNoMenu