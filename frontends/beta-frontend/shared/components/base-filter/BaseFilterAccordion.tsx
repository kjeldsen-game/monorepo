import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Typography,
} from '@mui/material';
import React, { ReactNode } from 'react';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import FilterAltIcon from '@mui/icons-material/FilterAlt';

interface BaseFilterAccordionProps {
    filter: string
    children: ReactNode
}

const BaseFilterAccordion: React.FC<BaseFilterAccordionProps> = ({ filter, children }) => {

    return (
        <Accordion
            defaultExpanded={false}
            sx={{
                marginBottom: 2,
                boxShadow: 'none',
                background: '#F3F4F6',
                borderRadius: '8px !important',
            }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />} >
                <FilterAltIcon sx={{ color: '#555F6C' }} />
                <Typography fontWeight={'bold'} color={"#555F6C"} marginLeft={1}>{filter} Filter</Typography>
            </AccordionSummary>
            <AccordionDetails>
                {children}
            </AccordionDetails>
        </Accordion>
    );
};

export default BaseFilterAccordion;
