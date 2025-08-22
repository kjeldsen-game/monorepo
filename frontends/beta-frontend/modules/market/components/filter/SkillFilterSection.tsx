import React from 'react'
import SkillRowFilter from './SkillRowFilter'
import { SkillRanges } from 'modules/market/types/filterForm';

interface SkillFilterSectionProps {
    formValues: SkillRanges;
    handleInputChange: (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => void;
}

const SkillFilterSection: React.FC<SkillFilterSectionProps> = ({
    formValues,
    handleInputChange
}) => {
    return (
        <>
            <SkillRowFilter
                formValues={formValues}
                handleInputChange={handleInputChange} />
            <SkillRowFilter
                formValues={formValues}
                handleInputChange={handleInputChange}
                namePrefix="Potential "
            />
        </>
    )
}

export default SkillFilterSection