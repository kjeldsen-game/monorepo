import { Accordion, AccordionDetails, AccordionSummary, Typography } from '@mui/material'
import React from 'react'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { TrainingEventResponse } from 'modules/player/types/TrainingResponses';
import ReportDataGrid from './ReportDataGrid';
import CustomAccordion from '@/shared/components/custom-accordion/CustomAccordion';

interface TrainingReportProps {
    date: string,
    trainings: TrainingEventResponse[]
}

const TrainingReport: React.FC<TrainingReportProps> = ({ date, trainings }) => {

    return (
        <Accordion
            key={date}
            defaultExpanded={false}
            sx={{
                '&.Mui-expanded': {
                    marginY: 1,
                },
                marginY: 1,
                boxShadow: 'none',
                borderRadius: 1,
                border: '2px solid #F3F4F6 !important',
                "&::before": {
                    display: "none",
                },
            }}>
            <AccordionSummary sx={{ background: '#F3F4F6 !important' }} expandIcon={<ExpandMoreIcon />} >
                <Typography fontWeight={'bold'} color={"#555F6C"} marginLeft={1}>
                    Training Report from {date}
                </Typography>
            </AccordionSummary>
            <AccordionDetails sx={{ background: 'white', padding: 1 }}>
                <ReportDataGrid trainings={trainings} />
            </AccordionDetails>
        </Accordion>
    )
}

export default TrainingReport;