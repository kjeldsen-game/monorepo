import { AppBar, Box, Breadcrumbs, Typography } from '@mui/material'
import Link from 'next/link'
import { useRouter } from 'next/router'
import StarIcon from '@mui/icons-material/Star';

const Subheader = () => {

    const { pathname } = useRouter();
    const pathSegments = pathname.split('/').filter(Boolean);

    return (
        <AppBar
            position="fixed"
            data-testid="subheader"
            sx={{
                mt: { xs: '60px', sm: '68px' },
                height: '32px',
                zIndex: (theme) => theme.zIndex.drawer + 1,
                width: '100%',
                background: 'white',
                color: 'black',
                display: 'flex',
                justifyContent: 'center'
            }}
        >
            <Box
                data-tesid="subheader-box"
                display={'flex'}
                sx={{ alignItems: 'center' }}>
                <Typography sx={{ paddingLeft: '16px', fontSize: '12px' }}>
                    Welcome to <span style={{ fontWeight: 'bold' }}> KJELDSEN </span>{' '}
                    v.1.24
                </Typography>
                <Breadcrumbs aria-label="breadcrumb" data-testid="breadcrumbs"
                    sx={{ paddingLeft: '40px' }}>
                    {pathSegments.map((segment, index) => (
                        <Box key={index} display={'flex'}>
                            <StarIcon sx={{ color: '#0000008A', width: '16px', marginRight: '8px' }} />
                            <Link style={{ textDecoration: 'none', color: '#0000008A' }} color="inherit" href={`/`}>
                                {segment.charAt(0).toUpperCase() + segment.slice(1)}
                            </Link>
                            {index < pathSegments.length - 1 && ' / '}
                        </Box>
                    ))}
                </Breadcrumbs>
            </Box>
        </AppBar>
    )
}

export default Subheader