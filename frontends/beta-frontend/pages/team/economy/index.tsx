import type { NextPage } from 'next';
import Head from 'next/head';
import { Box, Typography } from '@mui/material';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import useSWR from 'swr';
import { connectorAPI } from '@/libs/fetcher';
import { useSession } from 'next-auth/react';
import EconomyView from '@/shared/components/Economy/EconomyView';

const Economy: NextPage = () => {
    const { data: userData, status: sessionStatus } = useSession({
        required: true,
    });

    const { data: data } = useSWR(
        userData?.user?.teamId ? `/team/${userData.user.teamId}/economy` : null,
        connectorAPI,
    );

    return (
        <>
            <Box>
                <Box
                    sx={{
                        display: 'flex',
                        marginBottom: '2rem',
                        alignItems: 'center',
                    }}>
                    <EconomyView economy={data} />
                </Box>
            </Box>
        </>
    );
};

export default Economy;
