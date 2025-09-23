import { theme } from '@/libs/material/theme'
import { ActualSkill, PlayerSkillRelevance } from '@/shared/models/player/Player'
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils'
import { Box, Grid, Typography } from '@mui/material'
import { getSkillRelevanceGradient } from 'modules/player/utils/SkillRelevanceUtils'
import React from 'react'

interface SkillRelevanceDetailsProps {
    skills: { [key: string]: ActualSkill }
}

const SkillRelevanceDetails: React.FC<SkillRelevanceDetailsProps> = ({ skills }) => {

    return (
        <Grid container spacing={0.5} sx={{ height: '100%', alignItems: 'center' }}>
            {Object.entries(skills).map(([name, { PlayerSkills }]) => (
                <Grid size={6} key={name}
                    display={{ xs: 'flex', sm: 'block' }}
                    justifyContent={{ xs: 'center', sm: 'start' }}>
                    <Box
                        sx={{
                            paddingX: 1,
                            height: '30px',
                            background: getSkillRelevanceGradient(PlayerSkills.playerSkillRelevance),
                            width: '150px',
                            borderRadius: '8px',
                            color: 'white',
                            display: 'flex',
                            alignItems: 'center',
                        }}
                    >
                        <Typography
                            sx={{
                                whiteSpace: 'nowrap',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                            }}
                        >
                            {PlayerSkills.actual} - {convertSnakeCaseToTitleCase(name)}
                        </Typography>
                    </Box>
                </Grid>
            ))}
        </Grid>

    )
}

export default SkillRelevanceDetails