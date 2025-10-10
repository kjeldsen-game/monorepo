import { render, screen, fireEvent } from '@testing-library/react';
import SkillRowFilter from '../SkillRowFilter';
import { mockFormValues } from 'modules/market/__mocks__/mockSkillRanges';

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
