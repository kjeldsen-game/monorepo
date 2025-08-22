import React from 'react';
import { render, screen } from '@testing-library/react';
import LineupFilters from '../LineupFilters';

// Mock LineupFilterButton
jest.mock('../LineupFilterButton', () => (props: any) => {
    return (
        <button onClick={() => props.handleClick(props.name)}>{props.name}</button>
    );
});

describe('LineupFilters', () => {
    const mockSetFilter = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
    });

    const filterOptions = ['ALL', 'GK', 'DEF', 'MID', 'FW', 'ACTIVE', 'BENCH', 'INACTIVE'];

    test.each(filterOptions)('renders button for filter "%s"', (filterName) => {
        render(<LineupFilters filter="ALL" setFilter={mockSetFilter} />);
        expect(screen.getByText(filterName)).toBeInTheDocument();
    });

    test.each(filterOptions)('clicking "%s" calls setFilter with correct value', (filterName) => {
        render(<LineupFilters filter="ALL" setFilter={mockSetFilter} />);
        const button = screen.getByText(filterName);
        button.click();
        expect(mockSetFilter).toHaveBeenCalledWith(filterName);
    });
});
