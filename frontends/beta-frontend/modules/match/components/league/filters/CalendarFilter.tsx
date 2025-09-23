import BaseFilterAccordion from '@/shared/components/base-filter/BaseFilterAccordion'
import CustomSelectInput from '@/shared/components/Common/CustomSelectInput'
import { MatchStatus } from 'modules/match/types/MatchResponses'
import React from 'react'

interface CalendarFilterProps {
    filter: string,
    handleFilterChange: (event) => void;
}

const CalendarFilter: React.FC<CalendarFilterProps> = ({ filter, handleFilterChange }) => {

    return (
        <BaseFilterAccordion filter='Calendar'>
            <CustomSelectInput
                title={'Status'}
                onChange={handleFilterChange}
                value={filter}
                values={MatchStatus}
            />
        </BaseFilterAccordion>
    )
}

export default CalendarFilter