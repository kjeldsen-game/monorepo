import { Accordion, AccordionDetails, AccordionSummary, Typography } from '@mui/material'
import React from 'react'
import FilterAltIcon from '@mui/icons-material/FilterAlt';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import CustomSelectInput from '@/shared/components/Common/CustomSelectInput';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { useTrainingFilter } from 'modules/player/contexts/TrainingFilterContext';

interface TrainingFilterProps {
}

const TrainingFilter: React.FC<TrainingFilterProps> = ({ }) => {

    const { position, handlePositionChange } = useTrainingFilter()

    return (
        <Accordion
            defaultExpanded={false}
            sx={{
                boxShadow: 'none',
                background: '#F3F4F6',
                borderRadius: '8px !important',
            }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />} >
                <FilterAltIcon sx={{ color: '#555F6C' }} />
                <Typography fontWeight={'bold'} color={"#555F6C"} marginLeft={1}>Training Filter</Typography>
            </AccordionSummary>
            <AccordionDetails>
                <CustomSelectInput
                    value={position}
                    title={'Position'}
                    values={PlayerPosition}
                    onChange={handlePositionChange}
                />
            </AccordionDetails>
        </Accordion>
    )
}

export default TrainingFilter