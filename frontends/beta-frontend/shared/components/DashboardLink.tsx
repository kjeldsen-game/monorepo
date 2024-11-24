import { ReactNode } from 'react';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { Grid, Link, Typography } from '@mui/material';

interface DashboardLinkProps {
    children: ReactNode;
}

function DashboardLink({ children }: DashboardLinkProps) {
    return (
        <Grid container mb={2}>
            <Link href={'/dashboard'} sx={{ color: '#FF3F84' }}>
                <ArrowBackIcon />
            </Link>
            <Typography sx={{ fontSize: '16px', opacity: '40%' }}>
                {children}
            </Typography>
        </Grid>
    );
}

export default DashboardLink;
