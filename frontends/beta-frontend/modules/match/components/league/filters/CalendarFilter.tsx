import CustomSelectInput from '@/shared/components/Common/CustomSelectInput'
import { MatchStatus } from 'modules/match/types/MatchResponses'
import React from 'react'
import CustomAccordion from '@/shared/components/custom-accordion/CustomAccordion';

interface CalendarFilterProps {
    filter: string,
    handleFilterChange: (event) => void;
}

const CalendarFilter: React.FC<CalendarFilterProps> = ({ filter, handleFilterChange }) => {

    return (
        <CustomAccordion title='Calendar Filter'>
            <CustomSelectInput
                title={'Status'}
                onChange={handleFilterChange}
                value={filter}
                values={MatchStatus}
            />
        </CustomAccordion>
    )
}

export default CalendarFilter