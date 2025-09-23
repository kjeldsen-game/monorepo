import { Box } from '@mui/material'
import React, { ReactNode } from 'react'
import LocalFireDepartmentSharpIcon from '@mui/icons-material/LocalFireDepartmentSharp';
import { getSkillRelevanceColors } from 'modules/player/utils/SkillRelevanceUtils';
import { PlayerSkillRelevance } from '@/shared/models/player/Player';

interface SkillRelevanceSummaryProps {
    relevance: PlayerSkillRelevance;
    children: ReactNode;
}

const SkillRelevanceSummary: React.FC<SkillRelevanceSummaryProps> = ({ children, relevance }) => {
    return (
        <Box display={'flex'} alignItems={'center'} fontWeight={'bold'} fontSize={'20px'} sx={{ color: `${getSkillRelevanceColors(relevance).start}` }} >
            <LocalFireDepartmentSharpIcon sx={{ fontSize: '20px', color: `${getSkillRelevanceColors(relevance).start}` }} />
            {children}
        </Box >
    )
}

export default SkillRelevanceSummary