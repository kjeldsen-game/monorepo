import { Box, Card, CardContent, CardHeader, Grid, Typography } from '@mui/material';
import Head from 'next/head';
import React, { ReactNode } from 'react';
import AuthFooter from './AuthFooter';

type AuthTitle = "Sign In" | "Sign Up"

interface AuthViewWrapperProps {
    title: AuthTitle;
    children: ReactNode;
}

const AuthViewWrapper: React.FC<AuthViewWrapperProps> = ({
    children,
    title,
}) => {

    return (
        <>
            <Head>
                <title>{title}</title>
            </Head>
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                }}>
                {children}
                <AuthFooter />
            </Box>
        </>
    );
};

export default AuthViewWrapper;
