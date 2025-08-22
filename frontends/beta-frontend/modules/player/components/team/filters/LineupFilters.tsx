import { Box } from '@mui/material'
import React from 'react'
import LineupFilterButton from './LineupFilterButton'

interface LineupFiltersProps {
    filter: string
    setFilter: (filter: string) => void;
}

const filterOptions = ['ALL', 'GK', 'DEF', 'MID', 'FW', 'ACTIVE', 'BENCH', 'INACTIVE'];

const LineupFilters: React.FC<LineupFiltersProps> = ({ filter, setFilter }) => {

    return (
        <Box paddingY={1}>
            {filterOptions.map((name) => (
                <LineupFilterButton
                    key={name}
                    active={filter}
                    name={name}
                    handleClick={setFilter}
                />
            ))}
        </Box>
    )
}

export default LineupFilters