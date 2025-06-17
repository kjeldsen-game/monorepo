import { AppBar } from '@mui/material'

const HeaderDivider = () => {
    return (
        <AppBar
            position="fixed"
            sx={{
                mt: { xs: '56px', sm: '64px' },
                height: '4px',
                zIndex: (theme) => theme.zIndex.drawer + 1,
                width: '100%',
                background: '#FF3F84',
            }}
        >
        </AppBar>)
}

export default HeaderDivider