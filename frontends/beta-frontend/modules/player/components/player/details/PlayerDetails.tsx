import Box from '@mui/material/Box';
import { Avatar, CircularProgress, Grid, Typography } from '@mui/material';
import { FC } from 'react';
import { Player, PlayerSkillRelevance } from '@/shared/models/player/Player';
import SkillRelevanceDetails from '../skill-relevance/SkillRelevanceDetails';
import SkillRelevanceSummary from '../skill-relevance/SkillRelevanceSummary';
import ElderlyIcon from '@mui/icons-material/Elderly';

interface PlayerDetailsProps {
    player: Player;
};

export const PlayerDetails: FC<PlayerDetailsProps> = ({ player }) => {

    if (player === undefined) return <CircularProgress></CircularProgress>;

    return (
        <Grid container spacing={0}>
            <Grid size={{ xs: 12, sm: 3 }}>
                <Box display={'flex'} sx={{justifyContent: {xs: 'center', sm: 'normal'}}}>
                    <Avatar
                        sx={{ width: '100px', height: '100px', border: '1px solid #FF3F84' }}
                        alt="Upload new avatar"
                        src={false ? `data:image/jpeg;base64,${false}` : undefined} />
                    <Box display={'flex'} flexDirection={'column'} justifyContent={'center'} ml={2}>
                        <Typography fontSize={20} fontWeight={'bold'}>{player.name}</Typography>
                        <Typography fontWeight={'bold'}>{player.preferredPosition}</Typography>
                        <Box display={'flex'}>
                            <ElderlyIcon /> <Typography>{player.age.years}</Typography>
                        </Box>
                        <Box display={'flex'}>
                            <SkillRelevanceSummary relevance={PlayerSkillRelevance.CORE}>
                                88
                            </SkillRelevanceSummary>
                            <SkillRelevanceSummary relevance={PlayerSkillRelevance.RESIDUAL}>
                                88
                            </SkillRelevanceSummary>
                            <SkillRelevanceSummary relevance={PlayerSkillRelevance.SECONDARY}>
                                88
                            </SkillRelevanceSummary>
                        </Box>
                    </Box>
                </Box>
            </Grid>
            <Grid size={{ xs: 12, sm: 3 }}>
                <SkillRelevanceDetails skills={player.actualSkills} />
            </Grid>
        </Grid>
    );
};

export default PlayerDetails;
