import { theme } from '@/libs/material/theme'
import { alpha, Avatar, Box, Button, Card, CardActions, CardContent, CardMedia, Grid, IconButton, Typography } from '@mui/material'
import React, { useState } from 'react'
import CustomButton from '../Common/CustomButton'
import ArrowRightAltIcon from '@mui/icons-material/ArrowRightAlt';
import NewsCard from './NewsCard';
import ArrowBack from '@mui/icons-material/ArrowBack';
import { ArrowForward, Key, KeyboardArrowLeft, KeyboardArrowRight, TrendingUpOutlined } from '@mui/icons-material';
import CustomModal from '../CustomModal';
import { useModalManager } from '@/shared/hooks/useModalManager';
import NewsDialog from './NewsDialog';

const DashboardView = () => {
    const items = [1, 2, 3, 4, 5, 6, 7, 8, 9]; // your data
    const itemsPerPage = 3;
    const totalPages = Math.ceil(items.length / itemsPerPage);

    const [page, setPage] = useState(0);

    const nextPage = () => setPage((prev) => (prev + 1) % totalPages);
    const prevPage = () => setPage((prev) => (prev - 1 + totalPages) % totalPages);

    const { open, setOpen, handleCloseModal } = useModalManager();

    return (
        <>
            <NewsDialog open={open} handleClose={handleCloseModal} />
            <Box>
                <Card sx={{ padding: 1, boxShadow: 'none', border: '1px solid', borderColor: theme.palette.tertiary.main }}>
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
                                <Avatar sx={{ width: '60px', height: '60px' }}/>
                            </Box>
                        </Grid>
                    </Grid>
                </Card>

                <Grid container spacing={2} mt={2}>
                    <Grid size={{ md: 8, sm: 8, xs: 12 }} >

                        <Card sx={{ padding: 2, boxShadow: 'none', border: '1px solid', borderColor: theme.palette.tertiary.main }}>
                            <Typography
                                //  border={'1px solid red'}
                                fontWeight={'bold'} color={"#555F6C"} fontSize={'1.4rem'} pb={2}>
                                Fixtures
                            </Typography>
                            <Box sx={{ background: "#F9FAFB", padding: 1, paddingX: 2, borderRadius: 1 }} display={'flex'} alignItems={'center'} justifyContent={'space-between'}>
                                <Typography color={theme.palette.quaternary.main} fontSize={'16px'}>22 Jul 2024</Typography>
                                <Box display={'flex'} alignItems={'center'}>
                                    <Typography>Home Team</Typography>
                                    <Avatar sx={{ ml: { xs: 1, sm: 2 } }} />
                                    <Box sx={{ background: '#DCFCE7', paddingX: 1.5, paddingY: 0.5, borderRadius: 2, mx: { xs: 1, sm: 2 } }}>
                                        <Typography sx={{ color: '#16803C', fontWeight: 'bold' }}>3-1</Typography>
                                    </Box>
                                    <Avatar sx={{ mr: { xs: 1, sm: 2 } }} />
                                    <Typography>Away Team</Typography>
                                </Box>
                                <TrendingUpOutlined sx={{ color: theme.palette.quaternary.main }} />
                            </Box>
                            <Box sx={{ background: "#F9FAFB", padding: 1, paddingX: 2, borderRadius: 1, mt: 1 }} display={'flex'} alignItems={'center'} justifyContent={'space-between'}>
                                <Typography color={theme.palette.quaternary.main} fontSize={'16px'}>22 Jul 2024</Typography>
                                <Box display={'flex'} alignItems={'center'}>
                                    <Typography>Home Team</Typography>
                                    <Avatar sx={{ ml: { xs: 1, sm: 2 } }} />
                                    <Box sx={{ background: '#FFE2E2', paddingX: 1.5, paddingY: 0.5, borderRadius: 2, mx: { xs: 1, sm: 2 } }}>
                                        <Typography sx={{ color: '#B91C1B', fontWeight: 'bold' }}>3-1</Typography>
                                    </Box>
                                    <Avatar sx={{ mr: { xs: 1, sm: 2 } }} />
                                    <Typography>Away Team</Typography>
                                </Box>
                                <TrendingUpOutlined sx={{ color: theme.palette.quaternary.main }} />
                            </Box>
                            <Box sx={{ background: "#F9FAFB", padding: 1, paddingX: 2, borderRadius: 1, mt: 1 }} display={'flex'} alignItems={'center'} justifyContent={'space-between'}>
                                <Typography color={theme.palette.quaternary.main} fontSize={'16px'}>22 Jul 2024</Typography>
                                <Box display={'flex'} alignItems={'center'}>
                                    <Typography>Home Team</Typography>
                                    <Avatar sx={{ ml: { xs: 1, sm: 2 } }} />
                                    <Box sx={{ background: '#DCFCE7', paddingX: 1.5, paddingY: 0.5, borderRadius: 2, mx: { xs: 1, sm: 2 } }}>
                                        <Typography sx={{ color: '#16803C', fontWeight: 'bold' }}>3-1</Typography>
                                    </Box>
                                    <Avatar sx={{ mr: { xs: 1, sm: 2 } }} />
                                    <Typography>Away Team</Typography>
                                </Box>
                                <TrendingUpOutlined sx={{ color: theme.palette.quaternary.main }} />
                            </Box>

                        </Card>
                    </Grid>
                    <Grid size={{ md: 4, sm: 4, xs: 12 }} >

                        <Card sx={{ padding: 2, boxShadow: 'none', height: '100%', border: '1px solid', borderColor: theme.palette.tertiary.main }}>
                            <Typography fontWeight={'bold'} color={"#555F6C"} fontSize={'1.4rem'}>
                                My Team
                            </Typography>

                        </Card>
                    </Grid>
                </Grid>
                <Card sx={{ p: 2, mt: 2, boxShadow: 'none', border: '1px solid', borderColor: theme.palette.tertiary.main }}>

                    {/* HEADER ROW (title + buttons) */}
                    <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                        <Typography fontWeight="bold" color="#555F6C" fontSize="1.4rem">
                            Latest
                        </Typography>

                        <Box>
                            <IconButton
                                onClick={prevPage}
                                sx={{
                                    height: 32,
                                    width: 32,
                                    border: '1px solid',
                                    borderColor: theme.palette.tertiary.main,
                                    mr: 1
                                }}
                            >
                                <KeyboardArrowLeft
                                    sx={{ color: theme.palette.quaternary.main }}
                                />
                            </IconButton>

                            <IconButton
                                onClick={nextPage}
                                sx={{
                                    height: 32,
                                    width: 32,
                                    border: '1px solid',
                                    borderColor: theme.palette.tertiary.main,
                                }}
                            >
                                <KeyboardArrowRight
                                    sx={{ color: theme.palette.quaternary.main }}
                                />
                            </IconButton>
                        </Box>
                    </Box>

                    {/* CAROUSEL CONTAINER */}
                    <Box position="relative" overflow="hidden" width="100%">
                        <Box
                            display="flex"
                            width={`${totalPages * 100}%`}
                            sx={{
                                transform: `translateX(-${(page * 100) / totalPages}%)`,
                                transition: "transform 0.5s ease",
                            }}
                        >
                            {Array.from({ length: totalPages }).map((_, pageIndex) => (
                                <Grid
                                    container
                                    spacing={2}
                                    key={pageIndex}
                                    sx={{
                                        width: `${100 / totalPages}%`,
                                        flexShrink: 0,
                                    }}
                                >
                                    {items
                                        .slice(pageIndex * itemsPerPage, (pageIndex + 1) * itemsPerPage)
                                        .map((item, idx) => (
                                            <Grid size={4} key={idx}>
                                                <NewsCard id={item} openModal={setOpen} />
                                            </Grid>
                                        ))}
                                </Grid>
                            ))}
                        </Box>
                    </Box>

                </Card>
            </Box >
        </>
    )
}

export default DashboardView