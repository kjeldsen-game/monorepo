import { Accordion, AccordionDetails, AccordionSummary, Typography } from '@mui/material'
import React from 'react'
import FilterAltIcon from '@mui/icons-material/FilterAlt';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import CustomSelectInput from '@/shared/components/Common/CustomSelectInput';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { useTrainingFilter } from 'modules/player/contexts/TrainingFilterContext';
import BaseFilterAccordion from '@/shared/components/custom-accordion/CustomAccordion';
import CustomAccordion from '@/shared/components/custom-accordion/CustomAccordion';

interface TrainingFilterProps {
}

const TrainingFilter: React.FC<TrainingFilterProps> = ({ }) => {

    const { position, handlePositionChange } = useTrainingFilter()

    return (
        <CustomAccordion title='Training Filter'>
            <CustomSelectInput
                value={position}
                title={'Position'}
                values={PlayerPosition}
                onChange={handlePositionChange}
            />
        </CustomAccordion>
    )
}

export default TrainingFilter