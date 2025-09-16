import React, { useEffect, useRef, useState } from 'react';
import { Box, SxProps, Typography, useMediaQuery, useTheme } from '@mui/material';
import { ActualSkill } from '@/shared/models/player/Player';

interface PlayerSkillTextProps {
    skills?: ActualSkill;
    sx?: SxProps;
}

const PlayerSkillText: React.FC<PlayerSkillTextProps> = ({ skills, sx }) => {
    if (!skills) return null;
    const theme = useTheme();
    const isXs = useMediaQuery(theme.breakpoints.only('xs'));
    const boxRef = useRef<HTMLDivElement | null>(null);
    const [isTooSmall, setIsTooSmall] = useState(false);

    useEffect(() => {
        if (boxRef.current) {
            const { width } = boxRef.current.getBoundingClientRect();
            setIsTooSmall(width <= 30);
        }
    }, [skills, isXs]);

    const useColumn = isXs || isTooSmall;

    return (
        <Box
            ref={boxRef}
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
                flexDirection: useColumn ? 'column' : 'row',
                ...sx

            }}
        >
            <Typography fontWeight="bold">{skills.PlayerSkills.actual}</Typography>
            {!useColumn && '/'}
            <Typography fontSize="12px" color="#000000DE">
                {skills.PlayerSkills.potential}
            </Typography>
        </Box>
    );
};

export default PlayerSkillText;
