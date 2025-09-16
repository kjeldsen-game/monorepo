import { Accordion, AccordionDetails, AccordionSummary, Typography } from '@mui/material'
import React from 'react'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { TrainingEventResponse } from 'modules/player/types/TrainingResponses';
import ReportDataGrid from './ReportDataGrid';

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
                marginY: '8px',
                boxShadow: 'none',
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
            <AccordionDetails sx={{ background: 'white' }}>
                <ReportDataGrid trainings={trainings} />
            </AccordionDetails>
        </Accordion>
    )
}

export default TrainingReport;