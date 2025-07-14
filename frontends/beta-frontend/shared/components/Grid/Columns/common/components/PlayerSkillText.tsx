import React from 'react';
import { Box, Typography, useMediaQuery, useTheme } from '@mui/material';
import { ActualSkill } from '@/shared/models/player/Player';

interface PlayerSkillTextProps {
    skills?: ActualSkill;
}

const PlayerSkillText: React.FC<PlayerSkillTextProps> = ({ skills }) => {
    if (!skills) return null;
    const theme = useTheme();
    const isXs = useMediaQuery(theme.breakpoints.only('xs'));

    return (
        <Box
            padding={0}
            textAlign="center"
            sx={{
                alignItems: 'center',
                justifyContent: 'center',
                height: '100%',
                display: 'flex',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                maxWidth: '100%',
                flexDirection: {
                    xs: 'column',
                    sm: 'row'
                }
            }}
        >
            <Typography fontWeight="bold">{skills.PlayerSkills.actual}</Typography>
            {!isXs && '/'}
            <Typography fontSize="12px" color="#000000DE">
                {skills.PlayerSkills.potential}
            </Typography>
        </Box>
    );
};

export default PlayerSkillText;
