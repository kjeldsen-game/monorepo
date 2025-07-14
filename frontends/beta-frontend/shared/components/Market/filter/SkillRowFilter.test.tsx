import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import SkillRowFilter from './SkillRowFilter';
import { SkillRanges } from 'hooks/useMarketFilterForm';

const mockFormValues: SkillRanges = {
    SC: { min: '1', max: '2', potentialMin: '3', potentialMax: '4' },
    OP: { min: '2', max: '3', potentialMin: '4', potentialMax: '5' },
    BC: { min: '3', max: '4', potentialMin: '5', potentialMax: '6' },
    PA: { min: '4', max: '5', potentialMin: '6', potentialMax: '7' },
    AE: { min: '5', max: '6', potentialMin: '7', potentialMax: '8' },
    CO: { min: '6', max: '7', potentialMin: '8', potentialMax: '9' },
    TA: { min: '7', max: '8', potentialMin: '9', potentialMax: '10' },
    DP: { min: '8', max: '9', potentialMin: '10', potentialMax: '11' },
};

describe('SkillRowFilter', () => {
    const mockOnChange = jest.fn();

    it('renders all offensive skill MinMaxInput fields', () => {
        render(
            <SkillRowFilter
                formValues={mockFormValues}
                handleInputChange={mockOnChange}
            />
        );

        const inputs = screen.getAllByPlaceholderText('-');
        expect(inputs).toHaveLength(16);
    });

    it('calls handleInputChange on change', () => {
        render(
            <SkillRowFilter
                formValues={mockFormValues}
                handleInputChange={mockOnChange}
            />
        );

        const minInputs = screen.getAllByLabelText('Min');
        fireEvent.change(minInputs[0], { target: { value: '9' } });

        expect(mockOnChange).toHaveBeenCalledTimes(1);
    });

    it('renders children if provided', () => {
        render(
            <SkillRowFilter
                formValues={mockFormValues}
                handleInputChange={mockOnChange}
            >
                <div data-testid="custom-child">Child Element</div>
            </SkillRowFilter>
        );

        expect(screen.getByTestId('custom-child')).toBeInTheDocument();
    });
});
