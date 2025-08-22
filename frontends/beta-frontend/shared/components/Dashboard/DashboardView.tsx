import { Avatar, Box, Card, Grid, Typography } from '@mui/material'
import React from 'react'

const DashboardView = () => {
    return (
        <>
            <Box>
                <Card sx={{ padding: 1 }}>
                    <Typography fontWeight={'bold'} color={"#555F6C"} marginLeft={1} fontSize={'1.4rem'}>
                        Next Game
                    </Typography>
                    <Grid container spacing={2}>


                        <Grid size={{ md: 4, sm: 4, xs: 4 }} p={1} >
                            <Box display={'flex'} alignItems={'center'} justifyContent={'flex-start'} flexDirection={{ md: 'row', xs: 'column' }}>
                                <Avatar sx={{ width: '60px', height: '60px' }}>

                                </Avatar>
                                <Typography fontWeight={'bold'} variant='h6' sx={{ marginLeft: { xs: 0, md: 2 } }}>
                                    Home team
                                </Typography>
                            </Box>
                        </Grid>

                        <Grid size={{ md: 4, sm: 4, xs: 4 }} display={'flex'} alignItems={'center'} justifyContent={'center'} >
                            <Box display={'flex'} justifyContent={'center'} alignItems={'center'} flexDirection={'column'}>
                                <Typography fontWeight={'bold'} color={'#FF3F84'} variant='h4'>
                                    VS
                                </Typography>
                                <Typography variant='body2' fontSize={8}>
                                    25th July 2024, 19:45
                                </Typography>
                                <Typography variant='body2' fontSize={8}>
                                    Home Stadium
                                </Typography>
                            </Box>
                        </Grid>

                        <Grid size={{ md: 4, sm: 4, xs: 4 }} p={1} >
                            <Box display={'flex'} alignItems={'center'} justifyContent={'flex-end'} flexDirection={{ md: 'row', xs: 'column-reverse' }}>
                                <Typography fontWeight={'bold'} variant='h6' sx={{ marginRight: { xs: 0, md: 2 } }}>
                                    Home team
                                </Typography>
                                <Avatar sx={{ width: '60px', height: '60px' }}>

                                </Avatar>
                            </Box>
                        </Grid>


                    </Grid>
                </Card>

                <Grid container spacing={1}>
                    <Grid size={{ md: 8, sm: 8, xs: 12 }} >

                        <Card sx={{ padding: 1, mt: 2 }}>
                            <Typography fontWeight={'bold'} color={"#555F6C"} marginLeft={1} fontSize={'1.4rem'}>
                                Fixtures
                            </Typography>

                        </Card>
                    </Grid>
                    <Grid size={{ md: 4, sm: 4, xs: 12 }} >

                        <Card sx={{ padding: 1, mt: 2 }}>
                            <Typography fontWeight={'bold'} color={"#555F6C"} marginLeft={1} fontSize={'1.4rem'}>
                                My Team
                            </Typography>

                        </Card>
                    </Grid>
                </Grid>
                <Card sx={{ padding: 1 }}>
                    <Typography fontWeight={'bold'} color={"#555F6C"} marginLeft={1} fontSize={'1.4rem'}>
                        League News
                    </Typography>
                </Card>


            </Box >
        </>
    )
}

export default DashboardView