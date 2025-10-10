import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Typography,
} from '@mui/material';
import React, { ReactNode } from 'react';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import FilterAltIcon from '@mui/icons-material/FilterAlt';

interface CustomAccordionProps {
    title: string
    children: ReactNode
}

const CustomAccordion: React.FC<CustomAccordionProps> = ({ title, children }) => {

    return (
        <Accordion
            defaultExpanded={false}
            sx={{
                '&.Mui-expanded': {
                    marginY: 1,
                },
                marginBottom: 1,
                boxShadow: 'none',
                background: '#F3F4F6',
                borderRadius: '8px !important',
            }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />} >
                <FilterAltIcon sx={{ color: '#555F6C' }} />
                <Typography fontWeight={'bold'} color={"#555F6C"} marginLeft={1}>{title}</Typography>
            </AccordionSummary>
            <AccordionDetails>
                {children}
            </AccordionDetails>
        </Accordion>
    );
};

export default CustomAccordion;
